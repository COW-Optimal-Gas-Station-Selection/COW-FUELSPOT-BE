package com.cow.fuelspot.domain.member.dto;

import com.cow.fuelspot.global.common.enums.FuelType;
import com.cow.fuelspot.domain.member.entity.Member;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 회원가입 요청 DTO (보안, 검증 목적)
@Getter
@NoArgsConstructor // JSON -> 자바 객체 변환
public class MemberSignupRequest {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 다시 한번 입력해주세요.")
    private String checkPassword;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하이어야 합니다.")
    private String nickname;

    @NotNull(message = "선호 유종은 필수 선택 값입니다.")
    private FuelType fuelType;

    @NotNull(message = "선호 반경은 필수 입력 값입니다.")
    @Min(value = 1, message = "반경은 최소 1km 이상이어야 합니다.")
    private Integer radius;

    // DTO -> Entity 변환 메서드
    // 암호회된 비밀번호를 매개변수로 받아 넣음
    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .email(this.email)
                .password(encodedPassword) // 암호화된 비밀번호 전달
                .nickname(this.nickname)
                .fuelType(this.fuelType)
                .radius(this.radius)
                .build();
    }
}
