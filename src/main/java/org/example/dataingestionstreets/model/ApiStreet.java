package org.example.dataingestionstreets.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    // Getters and Setters
    public int getStreetId() {
        return streetId;
    }

    public void setStreetId(int streetId) {
        this.streetId = streetId;
    }

    public int getRegion_code() {
        return region_code;
    }

    public void setRegion_code(int region_code) {
        this.region_code = region_code;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public int getCity_code() {
        return city_code;
    }

    public void setCity_code(int city_code) {
        this.city_code = city_code;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public int getStreet_code() {
        return street_code;
    }

    public void setStreet_code(int street_code) {
        this.street_code = street_code;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public String getStreet_name_status() {
        return street_name_status;
    }

    public void setStreet_name_status(String street_name_status) {
        this.street_name_status = street_name_status;
    }

    public int getOfficial_code() {
        return official_code;
    }

    public void setOfficial_code(int official_code) {
        this.official_code = official_code;
    }
}
