package org.example.dataingestionstreets.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CityResponse {
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        private List<CityRecord> records;

        public List<CityRecord> getRecords() {
            return records;
        }

        public void setRecords(List<CityRecord> records) {
            this.records = records;
        }
    }
}
