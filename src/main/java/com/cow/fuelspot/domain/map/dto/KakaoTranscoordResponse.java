package com.cow.fuelspot.domain.map.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoTranscoordResponse {

    @JsonProperty("documents")
    private List<Document> documents;

    @Getter
    @NoArgsConstructor
    public static class Document{
        @JsonProperty("x")
        private String x;

        @JsonProperty("y")
        private String y;
    }
}
