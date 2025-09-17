package org.example.dataingestionstreets.mapper;

import org.example.dataingestionstreets.model.ApiStreet;
import org.example.dataingestionstreets.model.Street;
import org.springframework.stereotype.Component;

@Component
public class StreetMapper {

    /**
     * Maps an ApiStreet object from the external API to our internal Street model.
     * @param apiStreet The object received from the API.
     * @return A new Street object with mapped data.
     */
    public Street toStreet(ApiStreet apiStreet) {
        if (apiStreet == null) {
            return null;
        }

        // We are using the constructor created by Lombok's @AllArgsConstructor
        // new Street(int streetId, String street_name)
        return new Street(
                apiStreet.getStreetId(),
                apiStreet.getStreet_name() != null ? apiStreet.getStreet_name().trim() : null
        );
    }
}