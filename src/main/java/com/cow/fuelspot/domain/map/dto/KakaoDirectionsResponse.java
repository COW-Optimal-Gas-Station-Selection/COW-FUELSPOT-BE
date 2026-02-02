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
public class KakaoDirectionsResponse {

    @JsonProperty("routes")
    private List<Route> routes;

    @Getter
    @NoArgsConstructor
    public static class Route {

        @JsonProperty("summary")
        private Summary summary;

        @JsonProperty("sections")
        private List<Section> sections;
    }

    @Getter
    @NoArgsConstructor
    public static class Summary {
        @JsonProperty("distance")
        private Integer distance;

        @JsonProperty("duration")
        private Integer duration;
    }

    @Getter
    @NoArgsConstructor
    public static class Section {
        @JsonProperty("roads")
        private List<Road> roads;
    }

    @Getter
    @NoArgsConstructor
    public static class Road {
        @JsonProperty("vertexes")
        private List<Double> vertexes;
    }
}
