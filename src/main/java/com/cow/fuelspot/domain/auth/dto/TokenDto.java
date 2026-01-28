package com.cow.fuelspot.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

// 토큰 정보 DTO
// 토큰과 관련된 모든 정보를 묶은 객체
@Getter
@Builder
public class TokenDto {
    private String grantType; // 인증 방식: "Bearer"
    private String accessToken; // Access Token (API 요청용 인증 토큰)
    private String refreshToken; // Refresh Token (토큰 재발급용 갱신 토큰)
    private Long accessTokenExpiresIn; // Access Token 만료 시간
}