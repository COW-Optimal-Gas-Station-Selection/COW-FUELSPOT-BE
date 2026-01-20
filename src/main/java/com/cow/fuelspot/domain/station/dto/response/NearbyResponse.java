package com.cow.fuelspot.domain.station.dto.response;

import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearbyResponse {
    private String id;
    private String name;
    private String brand;
    private Integer priceGasoline;
    private Integer priceDiesel;
    private Integer priceLpg;
    private Integer distance;
    private String lat;
    private String lon;


}