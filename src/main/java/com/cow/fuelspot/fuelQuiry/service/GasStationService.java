package com.cow.fuelspot.fuelQuiry.service;

import com.cow.fuelspot.fuelQuiry.dto.GasStationDto;
import com.cow.fuelspot.fuelQuiry.dto.GasStationRequest;
import com.cow.fuelspot.fuelQuiry.dto.StationDetailResponse;
import com.cow.fuelspot.fuelQuiry.entity.GasStation;
import com.cow.fuelspot.fuelQuiry.repository.GasStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GasStationService {

    private final GasStationRepository gasStationRepository;

    /**
     * 내 주변 주유소 조회
     */
    @Transactional
    public List<GasStationDto> getNearbyStations(GasStationRequest request) {
        return gasStationRepository.findNearbyStations(request);
    }

    /**
     * 주유소 상세 조회
     */
    @Transactional(readOnly = true)
    public StationDetailResponse getStationDetail(String stationId) {
        GasStation entity = gasStationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주유소를 찾을 수 없습니다. ID: " + stationId));

        // 엔티티를 상세 응답 DTO로 변환
        return StationDetailResponse.builder()
                .stationId(entity.getStationId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .address(entity.getAddress())
                .priceGasoline(entity.getPriceGasoline())
                .priceDiesel(entity.getPriceDiesel())
                .lat(entity.getLat())
                .lon(entity.getLon())
                .isSelf(entity.getIsSelf())
                .isCarWash(entity.getIsCarWash())
                .build();

    }
}
