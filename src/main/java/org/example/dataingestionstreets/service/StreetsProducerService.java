package org.example.dataingestionstreets.service;

import lombok.RequiredArgsConstructor;
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
        var entity = buildRequestEntity(formattedCityName);

        StreetInfoResponse response = fetchStreets(entity);

        validateResponse(response, cityName);

        return convertToStreets(response);
    }

    private HttpEntity<Map<String, Object>> buildRequestEntity(String cityName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> filters = Map.of("city_name", cityName);

        Map<String, Object> requestBody = Map.of(
                "resource_id", RESOURCE_ID,
                "filters", filters,
                "limit", 100_000
        );

        return new HttpEntity<>(requestBody, headers);
    }

    private StreetInfoResponse fetchStreets(HttpEntity<Map<String, Object>> entity) {
        try {
            return restTemplate.postForObject(API_URL, entity, StreetInfoResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch streets from API", e);
        }
    }

    private List<Street> convertToStreets(StreetInfoResponse response) {
        return response.getResult().getRecords().stream()
                .map(apiStreet -> {
                    Street street = new Street();
                    street.setStreetId(apiStreet.getStreetId());
                    street.setStreet_name(apiStreet.getStreet_name().trim());
                    return street;
                })
                .collect(Collectors.toList());
    }

    private void validateResponse(StreetInfoResponse response, String cityName) {
        if (response == null ||
                response.getResult() == null ||
                response.getResult().getRecords() == null ||
                response.getResult().getRecords().isEmpty()) {
            throw new RuntimeException("No streets found for city: " + cityName);
        }
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
