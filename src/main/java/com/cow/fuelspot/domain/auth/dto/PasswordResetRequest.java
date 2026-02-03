package com.cow.fuelspot.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 인증 완료 후, 새로운 비밀번호 변경 요청 DTO
@Getter
@NoArgsConstructor
public class PasswordResetRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email; // 이메일

    @NotBlank(message = "인증 코드를 입력해주세요.")
    private String code; // 인증 코드

    // 변경할 새로운 비밀번호
    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
    private String newPassword;

    @NotBlank(message = "새 비밀번호를 다시 한번 입력해주세요.")
    private String checkNewPassword;
}
