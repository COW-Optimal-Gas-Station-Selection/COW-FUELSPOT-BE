package com.cow.fuelspot.domain.station.service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cow.fuelspot.domain.map.dto.KakaoTranscoordResponse;
import com.cow.fuelspot.domain.map.service.KakaoMapService;
import com.cow.fuelspot.domain.station.client.GasStationApiClient;
import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.opinet.OilPriceDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import com.cow.fuelspot.domain.station.dto.request.FilterRequest;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OpinetService {

    private final GasStationApiClient gasStationApiClient;
    private final KakaoMapService kakaoMapService;
    //근처 조회
    public List<NearbyResponse> getNearbyGasStations(NearbyRequest request) {
        KakaoTranscoordResponse ktmResponse = kakaoMapService.getKTMCoords(
                String.valueOf(request.getLon()),
                String.valueOf(request.getLat())
        );
        if (ktmResponse != null && !ktmResponse.getDocuments().isEmpty()) {
            request.setLon(ktmResponse.getDocuments().get(0).getX());
            request.setLat(ktmResponse.getDocuments().get(0).getY());
        }

        Map<String, NearbyResponse.NearbyResponseBuilder> mergeMap = new LinkedHashMap<>();
        for (FuelType type : FuelType.values()) {
            OpinetNearbyDto[] dtos = gasStationApiClient.getNearbyGasStations(request, type);
            if (dtos != null) {
                mergeByFuelType(mergeMap, dtos, type);
            }
        }
        return mergeMap.entrySet().stream()
                .parallel()
                .map(entry -> {
                    String id = entry.getKey();
                    NearbyResponse.NearbyResponseBuilder builder = entry.getValue();
                    OpinetDetailDto detail = gasStationApiClient.getDetailGasStation(id);
                    if (detail != null) {
                        builder.address(detail.getAddressNew())
                                .tel(detail.getTel())
                                .isCarWash("Y".equals(detail.getCarWashYn()));
                        
                        if (detail.getOilPrices() != null && !detail.getOilPrices().isEmpty()) {
                             OilPriceDto firstPrice = detail.getOilPrices().get(0);
                             builder.tradeDate(firstPrice.getTradeDate())
                                    .tradeTime(firstPrice.getTradeTime());
                        }
                        
                        setPriceFromDetail(builder, detail.getOilPrices());
                    }
                    return builder.build();
                })
                .collect(Collectors.toList());
    }
    //세부사항 조회
    public OpinetDetailDto getDetailGasStation(String id) {
        return gasStationApiClient.getDetailGasStation(id);
    }
    //필터 조회
    public List<NearbyResponse> getFilteredStations(FilterRequest request) {
        KakaoTranscoordResponse ktmResponse = kakaoMapService.getKTMCoords(
                String.valueOf(request.getLon()),
                String.valueOf(request.getLat())
        );
        if (ktmResponse != null && !ktmResponse.getDocuments().isEmpty()) {
            request.setLon(ktmResponse.getDocuments().get(0).getX());
            request.setLat(ktmResponse.getDocuments().get(0).getY());
        }
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
                        (request.getIsStore() == null || !request.getIsStore() || "Y".equals(pair.d.getCvsYn()))&&
                        (request.getBrand()==null || request.getBrand().equals(pair.n.getBrand()))
                )
                .map(pair -> {
                    NearbyResponse.NearbyResponseBuilder builder = NearbyResponse.builder()
                            .id(pair.n.getId())
                            .name(pair.n.getName())
                            .brand(pair.n.getBrand())
                            .distance(pair.n.getDistance())
                            .address(pair.d.getAddressNew())
                            .tel(pair.d.getTel())
                            .isCarWash("Y".equals(pair.d.getCarWashYn()));

                    if (pair.d.getOilPrices() != null && !pair.d.getOilPrices().isEmpty()) {
                        OilPriceDto firstPrice = pair.d.getOilPrices().get(0);
                        builder.tradeDate(firstPrice.getTradeDate())
                                .tradeTime(firstPrice.getTradeTime());
                    }

                    KakaoTranscoordResponse wgs84 = kakaoMapService.getWGS84Coords(pair.n.getLon(), pair.n.getLat());
                    if (wgs84 != null && !wgs84.getDocuments().isEmpty()) {
                        builder.lat(String.valueOf(wgs84.getDocuments().get(0).getY()));
                        builder.lon(String.valueOf(wgs84.getDocuments().get(0).getX()));
                    } else {
                        builder.lat(pair.n.getLat());
                        builder.lon(pair.n.getLon());
                    }

                    setPriceFromDetail(builder, pair.d.getOilPrices());

                    return builder.build();
                })
                .collect(Collectors.toUnmodifiableList());
    }

    private void setPriceFromDetail(NearbyResponse.NearbyResponseBuilder builder, List<OilPriceDto> oilPrices) {
        if (oilPrices == null || oilPrices.isEmpty()) return;

        for (OilPriceDto priceDto : oilPrices) {
            if (priceDto.getType() == null) continue;

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
            NearbyResponse.NearbyResponseBuilder builder = mergeMap.computeIfAbsent(stationId, id -> {
                NearbyResponse.NearbyResponseBuilder b = NearbyResponse.builder()
                        .id(id)
                        .name(dto.getName())
                        .brand(dto.getBrand())
                        .distance(dto.getDistance());

                KakaoTranscoordResponse wgs84 = kakaoMapService.getWGS84Coords(dto.getLon(), dto.getLat());
                if (wgs84 != null && !wgs84.getDocuments().isEmpty()) {
                    b.lat(String.valueOf(wgs84.getDocuments().get(0).getY()));
                    b.lon(String.valueOf(wgs84.getDocuments().get(0).getX()));
                } else {
                    b.lat(dto.getLat());
                    b.lon(dto.getLon());
                }
                return b;
            });
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
}