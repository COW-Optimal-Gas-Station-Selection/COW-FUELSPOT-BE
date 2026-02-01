package com.cow.fuelspot.domain.station.dto.response;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.opinet.OilPriceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Getter
@Builder
public class DetailResponse {

    private String id;
    private String name;
    private String brand;
    private String address;
    private String tel;
    private Double lat;
    private Double lon;
    private boolean isCarWash;
    private boolean isStore;
    private Map<FuelType, Integer> prices; // List에서 Map으로 변경

    private String tradeDate;
    private String tradeTime;

    public static DetailResponse from(OpinetDetailDto dto) {
        if (dto == null) {
            throw new NoSuchElementException("조회된 주유소 정보가 없습니다.");
        }

        Map<FuelType, Integer> prices = new HashMap<>();
        String tradeDate = null;
        String tradeTime = null;
        String minFullDateTime = null;

        if (dto.getOilPrices() != null) {
            for (OilPriceDto priceDto : dto.getOilPrices()) {
                if (priceDto.getType() != null && priceDto.getPrice() != null) {
                    prices.put(priceDto.getType(), priceDto.getPrice());
                }

                String currentFull = priceDto.getTradeDate() + priceDto.getTradeTime();
                if (minFullDateTime == null || currentFull.compareTo(minFullDateTime) < 0) {
                    minFullDateTime = currentFull;
                    tradeDate = priceDto.getTradeDate();
                    tradeTime = priceDto.getTradeTime();
                }
            }
        }

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
                .prices(prices)
                .tradeDate(tradeDate)
                .tradeTime(tradeTime)
                .build();
    }
}