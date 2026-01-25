package com.cow.fuelspot.domain.member.dto;

import com.cow.fuelspot.domain.member.entity.FuelType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 내 정보 수정용 DTO
@Getter
@NoArgsConstructor
public class MemberUpdateRequest {

    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하이어야 합니다.")
    private String nickname;

    private FuelType fuelType;

    @Min(value = 1, message = "반경은 최소 1km 이상이어야 합니다.")
    private Integer radius;
}
