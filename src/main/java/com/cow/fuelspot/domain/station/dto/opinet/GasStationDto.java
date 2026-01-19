package com.cow.fuelspot.domain.station.dto.opinet;

import com.fasterxml.jackson.annotation.JsonProperty; // 이 import가 필요합니다.
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GasStationDto {

    @JsonProperty("UNI_ID")
    private String id;

    @JsonProperty("POLL_DIV_CO")
    private String brand;

    @JsonProperty("OS_NM")
    private String name;

    @JsonProperty("PRICE")
    private Integer price;

    @JsonProperty("DISTANCE") // API 응답 확인 필요 (보통 DIST 또는 DISTANCE)
    private Integer distance;

    @JsonProperty("GIS_X_COOR")
    private String lat;

    @JsonProperty("GIS_Y_COOR")
    private String len;
}