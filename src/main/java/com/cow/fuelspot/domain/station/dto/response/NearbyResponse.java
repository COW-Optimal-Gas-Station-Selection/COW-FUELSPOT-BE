package com.cow.fuelspot.domain.station.dto.response;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
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
}