package com.cow.fuelspot.domain.member.dto;

import com.cow.fuelspot.global.common.enums.FuelType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarRegisterRequest {
    @Schema(description = "제조사(브랜드)", example = "현대")
    private String brand;

    @Schema(description = "모델명", example = "아반떼")
    private String modelName;

    @Schema(description = "유종 (GASOLINE, DIESEL, LPG)", example = "GASOLINE")
    private FuelType fuelType;
}