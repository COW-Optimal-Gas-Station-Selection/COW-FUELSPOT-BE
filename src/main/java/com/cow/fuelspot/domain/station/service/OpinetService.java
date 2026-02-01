package com.cow.fuelspot.domain.station.service;

import com.cow.fuelspot.domain.station.client.GasStationApiClient;
import com.cow.fuelspot.domain.station.component.FuelCalculator;
import com.cow.fuelspot.domain.station.component.OpinetMapper;
import com.cow.fuelspot.domain.station.component.StationFilter;
import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetAverageDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetSidoAverageDto;
import com.cow.fuelspot.domain.station.dto.request.FilterRequest;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.cow.fuelspot.domain.station.dto.response.AverageStationResponse;
import com.cow.fuelspot.domain.station.dto.response.DetailResponse;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpinetService {

    private final GasStationApiClient gasStationApiClient;
    private final FuelCalculator fuelCalculator;
    private final StationFilter stationFilter;
    private final OpinetMapper opinetMapper;

    // 근처 주유소 조회
    public List<NearbyResponse> getNearbyGasStations(NearbyRequest request) {
        Map<String, NearbyResponse> mergeMap = new LinkedHashMap<>();

        for (FuelType type : FuelType.values()) {
            List<OpinetNearbyDto> dtos = gasStationApiClient.getNearbyGasStations(request, type);
            if (dtos != null) {
                for (OpinetNearbyDto dto : dtos) {
                    //없는 경우
                    if (mergeMap.containsKey(dto.getId())) {
                        mergeMap.get(dto.getId()).getPrices().put(type, dto.getPrice());
                    }//있는 경우
                    else {
                        mergeMap.put(dto.getId(), opinetMapper.toNearbyResponse(dto, type, dto.getPrice()));
                    }
                }
            }
        }

        return mergeMap.values().stream()
                .sorted(Comparator.comparingDouble(n -> fuelCalculator.calculateFuelConsumption(n, request.getFuelType())))
                .collect(Collectors.toList());
    }

    // 주유소 상세 조회
    public DetailResponse getDetailGasStation(String id) {
        OpinetDetailDto detailDto = gasStationApiClient.getDetailGasStation(id);
        return opinetMapper.toDetailResponse(detailDto);
    }

    // 필터 기반 주유소 조회
    public List<NearbyResponse> getFilteredStations(FilterRequest request) {
        List<OpinetNearbyDto> nearbyDtos = gasStationApiClient.getStation(request);
        if (nearbyDtos == null) return List.of();

        return nearbyDtos.stream()
                .parallel()
                .map(nearby -> new StationPair(nearby, gasStationApiClient.getDetailGasStation(nearby.getId())))
                .filter(pair -> stationFilter.isMatch(pair.nearby(), pair.detail(), request))
                .map(pair -> opinetMapper.toNearbyResponse(pair.nearby(), pair.detail()))
                .sorted(Comparator.comparingDouble(n -> fuelCalculator.calculateFuelConsumption(n, request.getFuelType())))
                .collect(Collectors.toList());
    }

    // 전국 평균 유가 조회
    public AverageStationResponse getAverageStation() {
        List<OpinetAverageDto> dtos = gasStationApiClient.getAverageGasStation();

        Map<FuelType, AverageStationResponse.AveragePriceInfo> prices = new HashMap<>();

        for (FuelType fuelType : FuelType.values()) {
            Integer average = opinetMapper.extractAveragePrice(dtos, fuelType);
            Double weeklyChange = opinetMapper.extractWeeklyChange(dtos, fuelType);

            prices.put(fuelType, AverageStationResponse.AveragePriceInfo.builder()
                    .average(average)
                    .weeklyChange(weeklyChange)
                    .build()
            );
        }


        return AverageStationResponse.of(prices);
    }

    //시도별 평균 유가 조회
    public AverageStationResponse getSidoAverageStation(String sido) {
        List<OpinetSidoAverageDto> dtos = gasStationApiClient.getsidoAverageGasStation(sido);

        Map<FuelType, AverageStationResponse.AveragePriceInfo> prices = new HashMap<>();

        for (FuelType fuelType : FuelType.values()) {
            Integer average = opinetMapper.extractSidoAveragePrice(dtos, fuelType);
            Double weeklyChange = opinetMapper.extractSidoWeeklyChange(dtos, fuelType);

            prices.put(fuelType, AverageStationResponse.AveragePriceInfo.builder()
                    .average(average)
                    .weeklyChange(weeklyChange)
                    .build()
            );
        }
        return AverageStationResponse.of(prices);
    }

    private record StationPair(OpinetNearbyDto nearby, OpinetDetailDto detail) {}
}