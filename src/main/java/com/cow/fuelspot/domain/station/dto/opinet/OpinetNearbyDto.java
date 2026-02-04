package com.cow.fuelspot.domain.station.dto.opinet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpinetNearbyDto {

    //출력시 반환
    @JsonProperty("id")
    //오피넷에서 들어오는 데이터 찾을 때 사용
    @JsonAlias("UNI_ID")
    private String id;

    @JsonProperty("brand")
    @JsonAlias("POLL_DIV_CD")
    private String brand;

    @JsonProperty("name")
    @JsonAlias("OS_NM")
    private String name;

    @JsonProperty("price")
    @JsonAlias("PRICE")
    private Integer price;

    @JsonProperty("distance")
    @JsonAlias("DISTANCE")
    private Integer distance;

    @JsonProperty("lat")
    @JsonAlias("GIS_Y_COOR")
    private Double lat;

    @JsonProperty("lon")
    @JsonAlias("GIS_X_COOR")
    private Double lon;
}