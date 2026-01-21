package com.cow.fuelspot.domain.station.service;

import com.cow.fuelspot.domain.station.client.GasStationApiClient;
import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpinetService {

    private final GasStationApiClient gasStationApiClient;
    //근처 주유소 조회
    public List<NearbyResponse> getNearbyGasStations(NearbyRequest request) {
        Map<String, NearbyResponse.NearbyResponseBuilder> mergeMap = new LinkedHashMap<>();
        //유종 별 조회
        for (FuelType type : FuelType.values()) {
            OpinetNearbyDto[] dtos = gasStationApiClient.getNearbyGasStations(request, type);
            if (dtos != null) {
                mergeByFuelType(mergeMap, dtos, type);
            }
        }
        return mergeMap.values().stream()
                .map(NearbyResponse.NearbyResponseBuilder::build)
                .collect(Collectors.toList());
    }
    //정보 합치기
    private void mergeByFuelType(Map<String, NearbyResponse.NearbyResponseBuilder> mergeMap,
                                 OpinetNearbyDto[] dtos,
                                 FuelType type) {
        for (OpinetNearbyDto dto : dtos) {
            String stationId = dto.getId();
            NearbyResponse.NearbyResponseBuilder builder = mergeMap.computeIfAbsent(stationId, id ->
                    NearbyResponse.builder()
                            .id(id)
                            .name(dto.getName())
                            .brand(dto.getBrand())
                            .distance(dto.getDistance())
                            .lat(dto.getLat())
                            .lon(dto.getLon())
            );
            fillPrice(builder, type, dto.getPrice());
        }
    }

    private void fillPrice(NearbyResponse.NearbyResponseBuilder builder, FuelType type, Integer price) {
        if (price == null || price == 0) return;
        switch (type) {
            case GASOLINE -> builder.priceGasoline(price);
            case DIESEL -> builder.priceDiesel(price);
            case LPG -> builder.priceLpg(price);
        }
    }

    public OpinetDetailDto getDetailGasStation(String id){
        return gasStationApiClient.getDetailGasStation(id);
    }
}