package com.cow.fuelspot.domain.station.dto.request;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NearbyRequest {
    @NotNull
    private Double lat;

    @NotNull
    private Double lon;

    @NotNull
    private Integer radius;

    public String getLatString() {
        return formatDouble(this.lat);
    }

    public String getLonString() {
        return formatDouble(this.lon);
    }

    private String formatDouble(Double value) {
        if (value == null) return null;
        if (value == value.longValue()) {
            return String.format("%d", value.longValue());
        }
        return String.valueOf(value);
    }
}