package com.cow.fuelspot.domain.station.dto.request;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NearbyRequest {
    @NotNull
    private Double lat;

    @NotNull
    private Double lon;

    @NotNull
    private Integer radius;

    @NotNull
    private FuelType fuelType;

    public String getLatString() {
        return formatDouble(this.lat);
    }

    public String getLonString() {
        return formatDouble(this.lon);
    }
    private String formatDouble(Double value) {
        if (value == null) return null;
        // 2000.0 -> "2000"으로 변환 (소수점 이하가 0이면 제거)
        if (value == value.longValue()) {
            return String.format("%d", value.longValue());
        }
        return String.valueOf(value);
    }
}