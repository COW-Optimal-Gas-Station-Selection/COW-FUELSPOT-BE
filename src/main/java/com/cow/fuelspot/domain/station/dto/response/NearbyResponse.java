package com.cow.fuelspot.domain.station.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearbyResponse {
    private String id;
    private String name;
    private String brand;
    private String address;
    private String tel;
    private boolean isCarWash;
    private Integer priceGasoline;
    private Integer priceDiesel;
    private Integer priceLpg;
    private String tradeDate;
    private String tradeTime;
    private Integer distance;
    private String lat;
    private String lon;
}