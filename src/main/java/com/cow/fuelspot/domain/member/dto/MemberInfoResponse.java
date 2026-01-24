package com.cow.fuelspot.domain.member.dto;

import com.cow.fuelspot.domain.member.entity.Member;
import com.cow.fuelspot.domain.member.entity.FuelType;
import lombok.Builder;
import lombok.Getter;

// 내 정보 응답 DTO
@Getter
@Builder
public class MemberInfoResponse {

    private Long memberId;
    private String email;
    private String nickname;
    private FuelType fuelType;
    private Integer radius;

    // Entity -> DTO 변환
    public static MemberInfoResponse from(Member member) {
        return MemberInfoResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .fuelType(member.getFuelType())
                .radius(member.getRadius())
                .build();
    }
}
