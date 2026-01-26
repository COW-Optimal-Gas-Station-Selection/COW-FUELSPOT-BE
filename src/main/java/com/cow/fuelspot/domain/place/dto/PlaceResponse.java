package com.cow.fuelspot.domain.place.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PlaceResponse {
    private List<Place> places;
    private boolean hasMore;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Place {
        private String id;
        private String name;
        private String address;
        private String category;
        private String phone;
        private Double lat;
        private Double lon;
    }

}