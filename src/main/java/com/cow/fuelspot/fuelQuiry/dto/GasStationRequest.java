package com.cow.fuelspot.fuelQuiry.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GasStationRequest {
    private Double lat;          // 위도
    private Double lon;          // 경도
    private Double radius;       // 반경
    private Integer sort;        // 정렬 코드 (1, 2, 3)
    private FuelType fuelType;   // 유종 Enum
    private String brand;        // 브랜드
    private Boolean isSelf;      // 셀프 여부
    private Boolean isCarWash;   // 세차장 여부

    public SortType getSortType() {
        return SortType.fromCode(this.sort);
    }

}
