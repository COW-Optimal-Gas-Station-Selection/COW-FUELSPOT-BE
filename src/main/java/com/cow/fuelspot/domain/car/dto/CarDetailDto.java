package com.cow.fuelspot.domain.car.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CarDetailDto {
    private String carName;     // 모델 풀네임 (아반떼 (CN7) 1.6 가솔린)
    private String year;        // 연식 (2024)
    private String fuelType;    // 유종 (휘발유)
    private Double efficiency;  // 연비 (15.3)
}