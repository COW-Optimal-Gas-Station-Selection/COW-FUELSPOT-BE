package com.cow.fuelspot.domain.auth.dto;

import com.cow.fuelspot.domain.member.entity.FuelType;
import lombok.Builder;
import lombok.Getter;

// 로그인 응답 DTO
// 로그인이 성공했을 때 클라이언트에게 내려주는 데이터
@Getter
@Builder
public class LoginResponse {
    private Boolean isSuccess;
    private String message;
    private Long memberId;
    private String nickname;
    private FuelType fuelType;
    private Integer radius;
    private TokenDto tokenDto; // 토큰 관련 정보
}
