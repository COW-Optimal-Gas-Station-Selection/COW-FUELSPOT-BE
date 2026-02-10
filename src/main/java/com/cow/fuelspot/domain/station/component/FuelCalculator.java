package com.cow.fuelspot.domain.station.component;

import com.cow.fuelspot.global.common.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FuelCalculator {

    private static final double DEFAULT_EFFICIENCY = 15.0;

    public double calculateFuelConsumption(
            NearbyResponse nearby,
            FuelType type,
            Double efficiency
    ) {
        if (nearby == null || type == null) {
            return Double.MAX_VALUE;
        }

        double finalEfficiency = efficiency == null || efficiency <= 0
                ? DEFAULT_EFFICIENCY
                : efficiency;

        Integer price = nearby.getPrices().get(type);
        if (price == null || price <= 0) {
            return Double.MAX_VALUE;
        }

        return (nearby.getDistance() / 1000.0 / finalEfficiency) * price;
    }
}

