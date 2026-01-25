package com.cow.fuelspot.domain.station.dto.request;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class FilterRequest{
        @NotNull
        private double lat;          // 위도
        @NotNull
        private Double lon;          // 경도
        @NotNull
        private Integer radius;       // 반경
        @NotNull
        private FuelType fuelType;   // 유종 Enum
        private String brand;        // 브랜드
        private Boolean isCarWash;   // 세차장 여부
        private Boolean isStore; // 편의점 존재 여부


}
