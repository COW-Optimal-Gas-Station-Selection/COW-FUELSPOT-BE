package com.cow.fuelspot.domain.station.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GasStationResponse {
    private Long stationId;
    private String name;
    private String brand;
    private Integer price;
    private Double distance;
    private Double lat;
    private Double lon;
}