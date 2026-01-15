package com.cow.fuelspot.fuelQuiry.dto;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class StationDetailResponse {
    private String stationId;
    private String name;
    private String brand;
    private String address;
    private Integer priceGasoline;
    private Integer priceDiesel;
    private Integer priceLpg;
    private Double lat;
    private Double lon;
    private Boolean isSelf;
    private Boolean isCarWash;
}
