package com.cow.fuelspot.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 인증 코드 검증 요청 DTO
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequest {
    // 인증 코드를 발송받은 이메일 주소
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    // 사용자가 입력한 인증 코드
    @NotBlank(message = "인증 코드를 입력해주세요.")
    private String code;
}
