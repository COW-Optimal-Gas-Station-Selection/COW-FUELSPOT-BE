package com.cow.fuelspot.domain.member.dto;

import com.cow.fuelspot.global.common.enums.FuelType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCarResponse {
    @Schema(description = "차량 모델명", example = "아반떼")
    private String carName;

    @Schema(description = "유종", example = "GASOLINE")
    private FuelType fuelType;

    @Schema(description = "공인 연비", example = "13.6")
    private Double fuelEfficiency;

    public static MemberCarResponse empty() {
        return MemberCarResponse.builder()
                .carName(null)
                .fuelType(null)
                .fuelEfficiency(0.0)
                .build();
    }
}