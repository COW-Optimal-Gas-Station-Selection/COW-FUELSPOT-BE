package com.cow.fuelspot.domain.station.dto.response;

import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NearbyResponse {
    private String id;
    private String name;
    private String brand;
    private Integer price;
    private Integer distance;
    private String lat;
    private String lon;


    public static NearbyResponse from(OpinetNearbyDto dto){
        return NearbyResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .brand(dto.getBrand())
                .price(dto.getPrice())
                .distance(dto.getDistance())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }
}