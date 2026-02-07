package com.cow.fuelspot.domain.station.component;

import com.cow.fuelspot.domain.favorite.service.FavoriteService;
import com.cow.fuelspot.domain.map.dto.KakaoTranscoordResponse;
import com.cow.fuelspot.domain.map.service.KakaoMapService;
import com.cow.fuelspot.global.common.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.opinet.*;
import com.cow.fuelspot.domain.station.dto.response.DetailResponse;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
//매핑을 위한 컴포넌트
@Component
@RequiredArgsConstructor
public class OpinetMapper {
    private final KakaoMapService kakaoMapService;
    private final FavoriteService  favoriteService;

    // 상세 조회용 변환
    public DetailResponse toDetailResponse(OpinetDetailDto dto) {
        TradeTimeInfo timeInfo = extractMinTradeTime(dto.getOilPrices());

        //오피넷용->카카오용
        KakaoTranscoordResponse response = kakaoMapService.convertKTMToWGS84(
                String.valueOf(dto.getLon()),
                String.valueOf(dto.getLat())
        );
        Double lon = Double.valueOf(response.getDocuments().get(0).getX());
        Double lat = Double.valueOf(response.getDocuments().get(0).getY());

        return DetailResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .brand(dto.getBrand())
                .address(dto.getAddressNew())
                .tel(dto.getTel())
                .lat(lat)
                .lon(lon)
                .carWash("Y".equals(dto.getCarWashYn()))
                .store("Y".equals(dto.getCvsYn()))
                .prices(extractPricesAsMap(dto.getOilPrices()))
                .tradeDate(timeInfo.date())
                .tradeTime(timeInfo.time())
                .build();
    }

    // 목록/필터 조회용 변환 (상세 데이터 기반)
    public NearbyResponse toNearbyResponse(OpinetNearbyDto nearby, OpinetDetailDto detail) {
        TradeTimeInfo timeInfo = extractMinTradeTime(detail.getOilPrices());
        //오피넷용->카카오용
        KakaoTranscoordResponse response = kakaoMapService.convertKTMToWGS84(
                String.valueOf(detail.getLon()),
                String.valueOf(detail.getLat())
        );
        Double lon = Double.valueOf(response.getDocuments().get(0).getX());
        Double lat = Double.valueOf(response.getDocuments().get(0).getY());
//        long favoriteCount = favoriteService.getFavoriteCount(detail.getId());
//        System.out.println(favoriteCount);
        return NearbyResponse.builder()
                .id(nearby.getId())
                .name(nearby.getName())
                .brand(nearby.getBrand())
                .distance(nearby.getDistance())
                .lat(lat)
                .lon(lon)
                .prices(extractPricesAsMap(detail.getOilPrices()))
                .address(detail.getAddressNew())
                .tel(detail.getTel())
                .carWash("Y".equals(detail.getCarWashYn()))
                .tradeDate(timeInfo.date)
                .tradeTime(timeInfo.time())
//                .favoriteCount(favoriteCount)
                .build();
    }


    // Map 형태로 가격 추출 (DetailResponse, NearbyResponse 공통 사용)
    private Map<FuelType, Integer> extractPricesAsMap(List<OilPriceDto> oilPrices) {
        if (oilPrices == null) return new HashMap<>();

        return oilPrices.stream()
                .filter(p -> p.getType() != null && p.getPrice() != null && p.getPrice() > 0)
                .collect(Collectors.toMap(
                        OilPriceDto::getType,
                        OilPriceDto::getPrice,
                        (existing, replacement) -> existing, // 중복 키가 있으면 기존 값 유지
                        LinkedHashMap::new // 순서 보장
                ));
    }


    private TradeTimeInfo extractMinTradeTime(List<OilPriceDto> oilPrices) {
        String minFull = null, date = null, time = null;
        if (oilPrices != null) {
            for (OilPriceDto p : oilPrices) {
                String currentFull = p.getTradeDate() + p.getTradeTime();
                if (minFull == null || currentFull.compareTo(minFull) < 0) {
                    minFull = currentFull;
                    date = p.getTradeDate();
                    time = p.getTradeTime();
                }
            }
        }
        return new TradeTimeInfo(date, time);
    }

    public Integer extractAveragePrice(List<OpinetAverageDto> dtos, FuelType fuelType) {
        return dtos.stream()
                .filter(dto -> dto.getProdCd().equals(fuelType.getCode()))
                .map(this::parsePrice)
                .findFirst()
                .orElse(0);
    }


    public Double extractWeeklyChange(List<OpinetAverageDto> dtos, FuelType fuelType) {
        return dtos.stream()
                .filter(dto -> dto.getProdCd().equals(fuelType.getCode()))
                .map(this::parseDiff)
                .findFirst()
                .orElse(0.0);
    }

    public Integer extractSidoAveragePrice(List<OpinetSidoAverageDto> dtos, FuelType fuelType) {
        return dtos.stream()
                .filter(dto -> dto.getProdCd().equals(fuelType.getCode()))
                .map(this::parsePriceSido)
                .findFirst()
                .orElse(0);
    }


    public Double extractSidoWeeklyChange(List<OpinetSidoAverageDto> dtos, FuelType fuelType) {
        return dtos.stream()
                .filter(dto -> dto.getProdCd().equals(fuelType.getCode()))
                .map(this::parseDiffSido)
                .findFirst()
                .orElse(0.0);
    }

    private Integer parsePrice(OpinetAverageDto dto) {
        Object val = dto.getPrice();
        if (val instanceof Number n) return n.intValue();
        if (val instanceof String s) {
            try {
                return (int) Double.parseDouble(s);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    private Double parseDiff(OpinetAverageDto dto) {
        Object val = dto.getDiff();
        if (val instanceof Number n) return n.doubleValue();
        if (val instanceof String s) {
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    private Integer parsePriceSido(OpinetSidoAverageDto dto) {
        Object val = dto.getPrice();
        if (val instanceof Number n) return n.intValue();
        if (val instanceof String s) {
            try {
                return (int) Double.parseDouble(s);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    private Double parseDiffSido(OpinetSidoAverageDto dto) {
        Object val = dto.getDiff();
        if (val instanceof Number n) return n.doubleValue();
        if (val instanceof String s) {
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    private record TradeTimeInfo(String date, String time) {}
}