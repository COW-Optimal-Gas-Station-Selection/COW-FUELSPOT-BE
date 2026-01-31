package com.cow.fuelspot.domain.station.component;

import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import com.cow.fuelspot.domain.station.dto.request.FilterRequest;
import org.springframework.stereotype.Component;
//필터링 위한 컴포넌트
@Component
public class StationFilter {
    public boolean isMatch(OpinetNearbyDto nearby, OpinetDetailDto detail, FilterRequest request) {
        if (detail == null) return false;

        boolean brandMatch = request.getBrand() == null || request.getBrand().equals(nearby.getBrand());
        boolean carWashMatch = request.getIsCarWash() == null || !request.getIsCarWash() || "Y".equals(detail.getCarWashYn());
        boolean storeMatch = request.getIsStore() == null || !request.getIsStore() || "Y".equals(detail.getCvsYn());

        return brandMatch && carWashMatch && storeMatch;
    }
}