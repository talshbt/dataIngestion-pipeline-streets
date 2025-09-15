package org.example.dataingestionstreets.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CityRecord {
    @JsonProperty("city_name")
    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
