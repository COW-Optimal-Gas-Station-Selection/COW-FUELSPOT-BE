package com.cow.fuelspot.domain.station.dto.response;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.opinet.OilPriceDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailResponse {
    private String id;
    private String name;
    private String brand;
    private String address;
    private String tel;
    private String lat;
    private String lon;
    private boolean isCarWash;
    private Integer priceGasoline;
    private Integer priceDiesel;
    private Integer priceLpg;
    private String tradeDate;
    private String tradeTime;


    public static DetailResponse from(OpinetDetailDto dto) {
        Integer gasoline = null;
        Integer diesel = null;
        Integer lpg = null;
        String tradeDate = null;
        String tradeTime = null;

        if (dto.getOilPrices() != null) {
            for (OilPriceDto priceDto : dto.getOilPrices()) {
                FuelType type = priceDto.getType();
                if (type == null) continue;
                switch (type) {
                    case GASOLINE -> gasoline = priceDto.getPrice();
                    case DIESEL -> diesel = priceDto.getPrice();
                    case LPG -> lpg = priceDto.getPrice();
                }
                //TODO 판단 기준
                tradeDate=priceDto.getTradeDate();
                tradeTime=priceDto.getTradeTime();


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
                .priceGasoline(gasoline)
                .priceDiesel(diesel)
                .priceLpg(lpg)
                .tradeDate(tradeDate)
                .tradeTime(tradeTime)
                .build();
    }
}