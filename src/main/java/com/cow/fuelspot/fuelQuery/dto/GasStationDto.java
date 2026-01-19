package com.cow.fuelspot.fuelQuery.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 파라미터 없는 생성자
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 (QueryDSL용)
@Builder
public class GasStationDto {

    private String stationId; // 반드시 String이어야 함 (엔티티와 일치)
    private String name;
    private String brand;
    private Integer price;
    private Double distance;
    private Double lat;
    private Double lon;
}