package org.example.dataingestionstreets.service;

import lombok.RequiredArgsConstructor;
import org.example.dataingestionstreets.client.StreetApiClient;
import org.example.dataingestionstreets.mapper.StreetMapper;
import org.example.dataingestionstreets.model.Cities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.example.dataingestionstreets.model.ApiStreet;
import org.example.dataingestionstreets.model.Street;
import org.example.dataingestionstreets.model.StreetInfoResponse;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StreetsProducerService {
    private final RestTemplate restTemplate;
    private final StreetApiClient apiClient;
    private final StreetMapper streetMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Value("${kafka.topic.streets}")
    private String streetsTopic;

    private static final String RESOURCE_ID = "1b14e41c-85b3-4c21-bdce-9fe48185ffca";
    private static final String API_URL = "https://data.gov.il/api/3/action/datastore_search";
    private static final Map<String, String> hebrewToEnglishCityNames = new HashMap<>();

    private final KafkaTemplate<String, List<Street>> kafkaTemplate;

    public void publishStreetsToKafka(String cityName) {
        var streets = getStreetsInCity(cityName);
        kafkaTemplate.send(streetsTopic, cityName, streets);
        System.out.println("Successfully sent " + streets.size() + " streets for " + cityName + " to Kafka topic: " + streetsTopic);
    }

    private List<Street> getStreetsInCity(String cityName) {
        String formattedCityName = Cities.HEBREW_FORMATTED_NAMES.get(cityName);

        List<ApiStreet> apiStreets = apiClient.fetchStreetsByCity(formattedCityName);
        return apiStreets.stream()
                .map(streetMapper::toStreet)
                .collect(Collectors.toList());

    }


    public ApiStreet getStreetInfoById(int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> filters = new HashMap<>();
        filters.put("_id", id);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("resource_id", RESOURCE_ID);
        requestBody.put("filters", filters);
        requestBody.put("limit", 1);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        StreetInfoResponse response = restTemplate.postForObject(API_URL, entity, StreetInfoResponse.class);

        if (response == null || response.getResult() == null || response.getResult().getRecords() == null || response.getResult().getRecords().isEmpty()) {
            throw new RuntimeException("No street found for id: " + id);
        }

        ApiStreet dbStreet = response.getResult().getRecords().get(0);
        String englishCityName = hebrewToEnglishCityNames.getOrDefault(dbStreet.getCity_name(), dbStreet.getCity_name());
        dbStreet.setCity_name(englishCityName);
        dbStreet.setRegion_name(dbStreet.getRegion_name().trim());
        dbStreet.setStreet_name(dbStreet.getStreet_name().trim());

        return dbStreet;
    }
}
