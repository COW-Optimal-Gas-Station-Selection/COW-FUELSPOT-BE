package com.cow.fuelspot.domain.station.dto.request;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NearbyRequest {
    @NotNull(message = "위도는 필수 입력값 입니다.")
    private Double lat;          // 위도
    @NotNull(message = "경도는 필수 입력값 입니다.")
    private Double lon;          // 경도
    @NotNull(message = "반경는 필수 입력값 입니다.")
    @Positive(message = "반경은 0보다 커야 합니다.")
    private Integer radius;       // 반경
    private FuelType fuelType;

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