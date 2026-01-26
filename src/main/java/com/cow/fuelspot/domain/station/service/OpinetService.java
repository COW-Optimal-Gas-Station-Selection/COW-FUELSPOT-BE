package com.cow.fuelspot.domain.station.service;

import com.cow.fuelspot.domain.station.client.GasStationApiClient;
import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.opinet.OilPriceDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetAverageDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.request.FilterRequest;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.cow.fuelspot.domain.station.dto.response.AverageStationResponse;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpinetService {
    private static final int efficiency =  15; // 연비 가상
    private final GasStationApiClient gasStationApiClient;
    //근처 조회
    public List<NearbyResponse> getNearbyGasStations(NearbyRequest request) {
        Map<String, NearbyResponse.NearbyResponseBuilder> mergeMap = new LinkedHashMap<>();
        for (FuelType type : FuelType.values()) {
            OpinetNearbyDto[] dtos = gasStationApiClient.getNearbyGasStations(request, type);
            if (dtos != null) {
                mergeByFuelType(mergeMap, dtos, type);
            }
        }
        return mergeMap.values().stream()
                .map(NearbyResponse.NearbyResponseBuilder::build)
                .sorted(Comparator.comparingDouble(
                        (NearbyResponse nearby) -> calculateFuelConsumption(nearby, request.getFuelType())
                ))
                .collect(Collectors.toList());

    }
    //세부사항 조회
    public OpinetDetailDto getDetailGasStation(String id) {
        return gasStationApiClient.getDetailGasStation(id);
    }
    //필터 조회
    public List<NearbyResponse> getFilteredStations(FilterRequest request) {
            OpinetNearbyDto[] nearbyDtos = gasStationApiClient.getStation(request);

            if (nearbyDtos == null) return List.of();

            return Arrays.stream(nearbyDtos)
                    .parallel()
                    .map(nearby -> {
                        OpinetDetailDto detail = gasStationApiClient.getDetailGasStation(nearby.getId());
                        return new Object() {
                            final OpinetNearbyDto n = nearby;
                            final OpinetDetailDto d = detail;
                        };
                    })
                    .filter(pair -> pair.d != null &&
                            (request.getIsCarWash() == null || !request.getIsCarWash() || "Y".equals(pair.d.getCarWashYn())) &&
                            (request.getIsStore() == null || !request.getIsStore() || "Y".equals(pair.d.getCvsYn())) &&
                            (request.getBrand() == null || request.getBrand().equals(pair.n.getBrand()))
                    )
                .<NearbyResponse>map(pair -> {
                    NearbyResponse.NearbyResponseBuilder builder = NearbyResponse.builder()
                            .id(pair.n.getId())
                            .name(pair.n.getName())
                            .brand(pair.n.getBrand())
                            .distance(pair.n.getDistance())
                            .lat(pair.n.getLat())
                            .lon(pair.n.getLon());

                    setPriceFromDetail(builder, pair.d.getOilPrices());

                    return builder.build();
                })
                    .sorted(Comparator.comparingDouble(
                            nearby -> calculateFuelConsumption((NearbyResponse) nearby, request.getFuelType())
                    ))
                .collect(Collectors.toUnmodifiableList());
    }
    //효율 계산
    private double calculateFuelConsumption(NearbyResponse nearby, FuelType type) {
        Integer price = 0;
        switch (type) {
            case GASOLINE -> price = nearby.getPriceGasoline();
            case DIESEL -> price = nearby.getPriceDiesel();
            case LPG -> price = nearby.getPriceLpg();
            case PREMIUM_GASOLINE -> price = nearby.getPricePremiumGasoline();
            case KEROSENE -> price = nearby.getPriceKerosene();
        }
        int safePrice = (price == null) ? 0 : price;
        return (nearby.getDistance()/1000/ efficiency) * safePrice;
    }

    public AverageStationResponse getAverageStation() {
        List<OpinetAverageDto> dtos = gasStationApiClient.getAverageGasStation();

        return AverageStationResponse.builder()
                .pricePremiumGasoline(extractPrice(dtos, FuelType.PREMIUM_GASOLINE))
                .priceGasoline(extractPrice(dtos, FuelType.GASOLINE))
                .priceDiesel(extractPrice(dtos, FuelType.DIESEL))
                .priceKerosene(extractPrice(dtos, FuelType.KEROSENE))
                .priceLpg(extractPrice(dtos, FuelType.LPG))
                .build();
    }

    private Integer extractPrice(List<OpinetAverageDto> dtos, FuelType fuelType) {
        return dtos.stream()
                .filter(dto -> dto.getProdCd().equals(fuelType.getCode()))
                .map(dto -> {
                    Object val = dto.getPrice();
                    if (val instanceof Number) {
                        return ((Number) val).intValue();
                    }
                    if (val instanceof String) {
                        try {
                            return (int) Double.parseDouble((String) val);
                        } catch (NumberFormatException e) {
                            return 0;
                        }
                    }
                    return 0;
                })
                .findFirst()
                .orElse(0);
    }

    private void setPriceFromDetail(NearbyResponse.NearbyResponseBuilder builder, List<OilPriceDto> oilPrices) {
        if (oilPrices == null || oilPrices.isEmpty()) return;

        for (OilPriceDto priceDto : oilPrices) {

            int price = priceDto.getPrice();
            switch (priceDto.getType()) {
                case GASOLINE -> builder.priceGasoline(price);
                case DIESEL -> builder.priceDiesel(price);
                case LPG -> builder.priceLpg(price);
            }
        }
    }

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
            case PREMIUM_GASOLINE ->builder.pricePremiumGasoline(price);
            case KEROSENE -> builder.priceKerosene(price);
        }
    }
}