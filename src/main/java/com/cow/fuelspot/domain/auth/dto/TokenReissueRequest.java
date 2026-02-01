package com.cow.fuelspot.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 토큰 재발급 요청 DTO
// 만료된 Access Token을 갱신하기 위해 클라이언트가 보내는 데이터
@Getter
@NoArgsConstructor
public class TokenReissueRequest {
    @NotBlank(message = "Access Token을 입력해주세요.")
    private String accessToken; // 만료된 기존 Access Token
    @NotBlank(message = "Refresh Token을 입력해주세요.")
    private String refreshToken; // 검증을 위한 Refresh Token
}