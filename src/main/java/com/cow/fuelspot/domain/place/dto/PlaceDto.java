package com.cow.fuelspot.domain.place.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PlaceDto {

    @JsonProperty("documents")
    private List<PlaceInfo> places;

    private Meta meta;

    public boolean isEnd() {
        return meta != null && meta.isEnd();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Meta {
        @JsonProperty("is_end")
        private boolean isEnd;

        @JsonProperty("total_count")
        private int totalCount;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PlaceInfo {
        @JsonProperty("id")
        private String id;

        @JsonProperty("place_name")
        private String name;

        @JsonProperty("road_address_name")//도로명 주소
        private String address;

        @JsonProperty("category_name")//예시: 교통,수송 > 지하철,전철 > 수도권1호선
        private String category;

        @JsonProperty("category_group_name")//예시: 지하철역 ->비어 있는 것이 있음
        private String tag;

        @JsonProperty("phone")
        private String phone;

        @JsonProperty("x")
        private Double lon;

        @JsonProperty("y")
        private Double lat;
    }
}