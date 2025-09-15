package org.example.dataingestionstreets.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiStreet {

    @JsonProperty("_id")
    private int streetId;
    private int region_code;
    private String region_name;
    private int city_code;
    private String city_name;
    private int street_code;
    private String street_name;
    private String street_name_status;
    private int official_code;

}
