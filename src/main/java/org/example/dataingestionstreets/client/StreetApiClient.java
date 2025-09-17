package org.example.dataingestionstreets.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dataingestionstreets.model.ApiStreet;
import org.example.dataingestionstreets.model.Cities;
import org.example.dataingestionstreets.model.StreetInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StreetApiClient {

    private final RestTemplate restTemplate;

    private static final String RESOURCE_ID = "1b14e41c-85b3-4c21-bdce-9fe48185ffca";
    private static final String API_URL = "https://data.gov.il/api/3/action/datastore_search";

    /**
     * Fetches all streets for a given Hebrew city name from the external API.
     */
    public List<ApiStreet> fetchStreetsByCity(String hebrewCityName) {
        Map<String, Object> filters = Map.of("city_name", hebrewCityName);
        StreetInfoResponse response = postToApi(filters, 100_000);

        return Optional.ofNullable(response)
                .map(res -> res.getResult().getRecords())
                .orElse(Collections.emptyList());
    }

    /**
     * Fetches a single street by its unique ID from the external API.
     */
    public Optional<ApiStreet> fetchStreetById(int id) {
        Map<String, Object> filters = Map.of("_id", id);
        StreetInfoResponse response = postToApi(filters, 1);

        return Optional.ofNullable(response)
                .map(res -> res.getResult().getRecords())
                .filter(records -> !records.isEmpty())
                .map(records -> records.get(0));
    }

    private StreetInfoResponse postToApi(Map<String, Object> filters, int limit) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "resource_id", RESOURCE_ID,
                "filters", filters,
                "limit", limit
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            return restTemplate.postForObject(API_URL, entity, StreetInfoResponse.class);
        } catch (RestClientException e) {
            log.error("API call failed for filters: {}. Error: {}", filters, e.getMessage());
            // It's better to throw a custom exception here to be handled by the service layer
            throw new RuntimeException("Failed to fetch data from API", e);
        }
    }
}