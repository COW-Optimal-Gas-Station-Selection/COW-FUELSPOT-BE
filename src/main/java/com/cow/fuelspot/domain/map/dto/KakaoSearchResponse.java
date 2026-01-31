package com.cow.fuelspot.domain.map.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.swing.text.Document;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoSearchResponse {

    @JsonProperty("documents")
    private List<Document> documents;

    @Getter
    @NoArgsConstructor
    public static class Document {
        @JsonProperty("place_name")
        private String placeName;

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("road_address_name")
        private String roadAddressName;

        @JsonProperty("x")
        private String x;

        @JsonProperty("y")
        private String y;
    }
}
