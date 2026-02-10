package com.cow.fuelspot.domain.car.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CachedCar {
    private String brand;
    private String modelName;
    private String fuelType;
    private Double fuelEfficiency;
}