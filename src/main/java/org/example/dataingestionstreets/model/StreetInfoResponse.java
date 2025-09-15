package org.example.dataingestionstreets.model;


import java.util.List;

public class StreetInfoResponse {

    private Result result;

    // Getters and Setters
    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        private List<ApiStreet> records;

        public List<ApiStreet> getRecords() {
            return records;
        }

        public void setRecords(List<ApiStreet> records) {
            this.records = records;
        }
    }
}
