package com.cow.fuelspot.domain.car.dto;

import com.cow.fuelspot.global.common.enums.FuelType;
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
    private FuelType fuelType;
    private Double fuelEfficiency;
}