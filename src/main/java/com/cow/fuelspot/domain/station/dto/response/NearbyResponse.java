package com.cow.fuelspot.domain.station.dto.response;

import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;

import lombok.*;

@Getter
@Setter             // 쿼리 파라미터 주입을 위해 필요
@NoArgsConstructor  // 기본 생성자 필요
@AllArgsConstructor // Builder 사용 시 필요
@Builder
public class NearbyResponse {
    private String id;
    private String name;
    private String brand;
    private Integer priceGasoline;
    private Integer priceDiesel;
    private Integer priceLpg;
    private Integer distance;
    private String lat;
    private String lon;


    public static NearbyResponse from(OpinetNearbyDto dto){
        return NearbyResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .brand(dto.getBrand())
                .distance(dto.getDistance())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }
}