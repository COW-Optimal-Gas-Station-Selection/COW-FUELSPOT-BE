package com.cow.fuelspot.domain.station.component;

import com.cow.fuelspot.global.common.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.opinet.*;
import com.cow.fuelspot.domain.station.dto.response.DetailResponse;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
//매핑을 위한 컴포넌트
@Component
public class OpinetMapper {

    // 상세 조회용 변환
    public DetailResponse toDetailResponse(OpinetDetailDto dto) {
        TradeTimeInfo timeInfo = extractMinTradeTime(dto.getOilPrices());

        return DetailResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .brand(dto.getBrand())
                .address(dto.getAddressNew())
                .tel(dto.getTel())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .isCarWash("Y".equals(dto.getCarWashYn()))
                .isStore("Y".equals(dto.getCvsYn()))
                .prices(extractPricesAsMap(dto.getOilPrices()))
                .tradeDate(timeInfo.date())
                .tradeTime(timeInfo.time())
                .build();
    }

    // 목록/필터 조회용 변환 (상세 데이터 기반)
    public NearbyResponse toNearbyResponse(OpinetNearbyDto nearby, OpinetDetailDto detail) {
        return NearbyResponse.builder()
                .id(nearby.getId())
                .name(nearby.getName())
                .brand(nearby.getBrand())
                .distance(nearby.getDistance())
                .lat(nearby.getLat())
                .lon(nearby.getLon())
                .prices(extractPricesAsMap(detail.getOilPrices()))
                .build();
    }

    // 근처 주유소 기본 변환 (기본 데이터 기반)
    public NearbyResponse toNearbyResponse(OpinetNearbyDto dto, FuelType type, Integer price) {
        Map<FuelType, Integer> prices = new HashMap<>();
        prices.put(type, price);

        return NearbyResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .brand(dto.getBrand())
                .distance(dto.getDistance())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .prices(prices)
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