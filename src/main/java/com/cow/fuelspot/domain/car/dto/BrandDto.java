package com.cow.fuelspot.domain.car.dto;

import com.cow.fuelspot.domain.car.type.CarBrand;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BrandDto {
    private String name;      // "현대" (화면용)
    private String code;      // "HYUNDAI" (API 요청용)
    private String imagePath; // 로고 이미지 경로

    // Enum -> DTO 변환 편의 메서드
    public static BrandDto from(CarBrand brand) {
        return BrandDto.builder()
                .name(brand.getViewName())
                .code(brand.name()) // Enum 상수 이름 (HYUNDAI)
                .imagePath(brand.getImagePath())
                .build();
    }
}