package com.cow.fuelspot.domain.station.component;

import com.cow.fuelspot.global.common.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FuelCalculator {

    private static final double DEFAULT_EFFICIENCY = 15.0;
    // 제조사 설계 기준: 완충 시 평균 주행 거리 (km)
    private static final double DRIVING_RANGE_CONSTANT = 600.0;
    // 연료 경고등 시점 보정 계수 (약 15% 잔량 가정)
    private static final double FUEL_RESERVE_FACTOR = 0.85;

    public double calculateFuelConsum(
            NearbyResponse nearby,
            FuelType type,
            Double efficiency
    ) {
        if (nearby == null || type == null) {
            return Double.MAX_VALUE;
        }

        // 1. 연비 결정 (매개변수가 없으면 기본값 사용)
        double finalEfficiency = (efficiency == null || efficiency <= 0)
                ? DEFAULT_EFFICIENCY
                : efficiency;

        // 2. 해당 유종의 가격 확인
        Integer price = nearby.getPrices().get(type);
        if (price == null || price <= 0) {
            return Double.MAX_VALUE;
        }

        // 3. [추가된 로직] 연비 기반 주유 예정량 추정 (L)
        // 공식: (600km / 연비) * 0.85
        double estimatedFillAmount = (DRIVING_RANGE_CONSTANT / finalEfficiency) * FUEL_RESERVE_FACTOR;

        // 4. 이동 비용 계산 (왕복 거리 고려: distance는 미터 단위이므로 / 1000.0)
        double roundTripDistanceKm = (nearby.getDistance() * 2) / 1000.0;
        double movingCost = (roundTripDistanceKm / finalEfficiency) * price;

        // 5. 실제 주유 비용 계산
        double refuelingCost = estimatedFillAmount * price;

        // 6. 최종 합산 비용 반환 (이 값이 가장 낮은 주유소가 최적)
        return refuelingCost + movingCost;
    }
}
