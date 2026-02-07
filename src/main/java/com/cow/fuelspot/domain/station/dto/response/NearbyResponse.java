package com.cow.fuelspot.domain.station.dto.response;

import com.cow.fuelspot.global.common.enums.FuelType;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearbyResponse {
    private String id;
    private String name;
    private String brand;
    private Map<FuelType, Integer> prices;
    private Integer distance;
    private Double lat;
    private Double lon;
    private String address;
    private String tel;
    private boolean carWash;
    private String tradeDate;
    private String tradeTime;
    private long favoriteCount;
}