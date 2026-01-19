package com.cow.fuelspot.domain.station.dto.response;

import com.cow.fuelspot.domain.station.dto.opinet.OilPriceDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
    private boolean isSelf;
    private boolean isCarWash;
    private List<OilPriceDto> prices;

    public static DetailResponse from(OpinetDetailDto dto) {
        List<OilPriceDto> priceDtos = new ArrayList<>();
        if (dto.getOIL_PRICE() != null) {
            for (OilPriceDto priceDetail : dto.getOIL_PRICE()) {
                priceDtos.add(OilPriceDto.builder()
                        .type(priceDetail.getType())
                        .price(priceDetail.getPrice())
                        .build());
            }
        }

        return DetailResponse.builder()
                .id(dto.getUNI_ID())
                .name(dto.getOS_NM())
                .brand(dto.getPOLL_DIV_CO())
                .address(dto.getNEW_ADR())
                .tel(dto.getTEL())
                .lat(dto.getGIS_X_COOR())
                .lon(dto.getGIS_Y_COOR())
                .isSelf("Y".equals(dto.getM_POLL_DIV_CO()))
                .isCarWash("Y".equals(dto.getCAR_WASH_YN()))
                .prices(priceDtos)
                .build();
    }
}
