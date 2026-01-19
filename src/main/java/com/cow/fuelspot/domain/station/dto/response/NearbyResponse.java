package com.cow.fuelspot.domain.station.dto.response;

import com.cow.fuelspot.domain.station.dto.opinet.GasStationDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
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
    private String len;


    public static NearbyResponse from(GasStationDto dto){
        return NearbyResponse.builder()
                .id(dto.getId())
                .name(dto.getName())      // 수정: name ← getName()
                .brand(dto.getBrand())    // 수정: brand ← getBrand()
                .price(dto.getPrice())
                .distance(dto.getDistance())
                .lat(dto.getLat())
                .len(dto.getLen())
                .build();
    }
}