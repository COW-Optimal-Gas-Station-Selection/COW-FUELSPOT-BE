package com.cow.fuelspot.fuelQuiry.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GasStationDto {

    private Long stationId;
    private String name;
    private String brand;
    private Integer price;
    private Double distance;
    private Double lat;
    private Double lon;

    @Builder
    public GasStationDto(Long stationId, String name, String brand, Integer price,
                         Double distance, Double lat, Double lon) {
        this.stationId = stationId;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.distance = distance;
        this.lat = lat;
        this.lon = lon;
    }

}