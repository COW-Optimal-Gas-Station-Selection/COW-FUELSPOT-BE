package com.cow.fuelspot.domain.station.component;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
//추천순 정렬을 위한 계산 컴포넌트
@Component
public class FuelCalculator {
    private static final int EFFICIENCY = 15;

    public double calculateFuelConsumption(NearbyResponse nearby, FuelType type) {
        Map<FuelType, Integer> prices = nearby.getPrices();
        Integer price = (prices != null) ? prices.get(type) : null;
        if (price == null || price <= 0) {
            return Double.MAX_VALUE;
        }
        return (nearby.getDistance() / 1000.0 / EFFICIENCY) * price;
    }
}