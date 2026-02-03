package com.cow.fuelspot.domain.station.dto.request;

import com.cow.fuelspot.global.common.enums.FuelType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FilterRequest{
        @NotNull(message = "위도는 필수 입력값 입니다.")
        private double lat;          // 위도
        @NotNull(message = "경도는 필수 입력값 입니다.")
        private Double lon;          // 경도
        @NotNull(message = "반경는 필수 입력값 입니다.")
        @Positive(message = "반경은 0보다 커야 합니다.")
        private Integer radius;       // 반경
        private FuelType fuelType;   // 유종 Enum
        private String brand;        // 브랜드
        private Boolean isCarWash;   // 세차장 여부
        private Boolean isStore; // 편의점 존재 여부


}
