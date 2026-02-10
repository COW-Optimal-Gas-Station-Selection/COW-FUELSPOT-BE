package com.cow.fuelspot.domain.car.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class CarResponse {
    private String modelName;
    private Double fuelEfficiency;
}