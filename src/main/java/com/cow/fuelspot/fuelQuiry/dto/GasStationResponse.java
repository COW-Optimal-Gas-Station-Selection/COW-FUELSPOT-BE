package com.cow.fuelspot.fuelQuiry.dto;

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