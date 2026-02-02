package com.cow.fuelspot.domain.station.dto.response;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import lombok.*;

import java.util.Map;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AverageStationResponse {
    private Map<FuelType, AveragePriceInfo> prices;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class AveragePriceInfo {
        private Integer average;              // 전국 평균 가격
        private Double weeklyChange;          // 주간 변동률 (%)
    }

    public static AverageStationResponse of(Map<FuelType, AveragePriceInfo> prices) {
        return new AverageStationResponse(prices);
    }
}