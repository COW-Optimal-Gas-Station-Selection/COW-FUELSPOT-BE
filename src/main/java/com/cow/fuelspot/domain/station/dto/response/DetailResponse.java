package com.cow.fuelspot.domain.station.dto.response;

import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.opinet.OilPriceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<PriceInfo> prices;

    @Getter
    @Builder
    public static class PriceInfo {
        private String type;
        private Integer price;
        private String tradeDate;
    }

    public static DetailResponse from(OpinetDetailDto dto) {
        List<PriceInfo> priceList = null;

        if (dto.getOilPrices() != null) {
            priceList = dto.getOilPrices().stream()
                    .map(p -> PriceInfo.builder()
                            .type(p.getType())
                            .price(p.getPrice())
                            .tradeDate(p.getTradeDate())
                            .build())
                    .collect(Collectors.toList());
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
                .prices(priceList)
                .build();
    }
}