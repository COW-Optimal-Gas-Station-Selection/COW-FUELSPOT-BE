package com.cow.fuelspot.domain.station.dto.request;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NearbyRequest {
    @NotBlank
    private String lat;

    @NotBlank
    private String lon;

    @NotNull
    private Integer radius;

    @NotNull
    private FuelType fuelType;
}
