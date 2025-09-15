//package org.example.dataingestionstreets.service;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.example.dataingestionstreets.model.CityResponse;
//import org.example.dataingestionstreets.model.CityRecord;
//import com.google.cloud.translate.v3.LocationName;
//import com.google.cloud.translate.v3.TranslateTextRequest;
//import com.google.cloud.translate.v3.TranslationServiceClient;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.stream.Collectors;
//
//@Service
//public class CitiesService {
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private static final String API_URL = "https://data.gov.il/api/3/action/datastore_search?resource_id=1b14e41c-85b3-4c21-bdce-9fe48185ffca&limit=10000&offset=0&fields=city_name&distinct=true&sort=city_name&include_total=false";
//    private static final String GOOGLE_API_KEY = "GOOGLE API SECRET KEY GOES HERE"; // Replace with your actual key
//
//    public void getAndTranslateCities() {
//        try {
//            // Get cities from the API
//            CityResponse citiesResponse = restTemplate.getForObject(API_URL, CityResponse.class);
//            if (citiesResponse == null || citiesResponse.getResult() == null || citiesResponse.getResult().getRecords() == null) {
//                throw new RuntimeException("Could not retrieve cities from API.");
//            }
//
//            // Create a Google Translate client
//            TranslationServiceClient client = TranslationServiceClient.create();
//            LocationName parent = LocationName.of("your-project-id", "global"); // Replace with your Google Cloud Project ID
//
//            // Translate and save to a file
//            try (FileWriter fileWriter = new FileWriter("cities.json")) {
//                fileWriter.write("{\n");
//                for (CityRecord record : citiesResponse.getResult().getRecords()) {
//                    String hebrewCity = record.getCityName();
//                    TranslateTextRequest request = TranslateTextRequest.newBuilder()
//                            .setParent(parent.toString())
//                            .setMimeType("text/plain")
//                            .setTargetLanguageCode("en")
//                            .addContents(hebrewCity)
//                            .build();
//
//                    String englishCity = client.translateText(request).getTranslations(0).getTranslatedText();
//                    fileWriter.write(String.format("  \"%s\" : \"%s\",\n", englishCity, hebrewCity));
//                }
//                // Add a final entry to handle the trailing comma
//                fileWriter.write("}\n");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("An error occurred while writing to the file.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("An error occurred during API call or translation.");
//        }
//    }
//}