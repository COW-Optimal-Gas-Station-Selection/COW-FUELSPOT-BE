package com.cow.fuelspot.domain.station.dto.request;

import com.cow.fuelspot.global.common.enums.FuelType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

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
}