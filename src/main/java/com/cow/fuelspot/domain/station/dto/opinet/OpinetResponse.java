package com.cow.fuelspot.domain.station.dto.opinet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpinetResponse<T> {

    @JsonProperty("RESULT")
    private Result<T> result;

    public List<T> getOilList() {
        return (result != null && result.oil != null) ? result.oil : Collections.emptyList();
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Result<T> {
        @JsonProperty("OIL")
        private List<T> oil;
    }
}
