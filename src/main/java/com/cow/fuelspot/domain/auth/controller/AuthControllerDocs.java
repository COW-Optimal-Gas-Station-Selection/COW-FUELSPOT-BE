package com.cow.fuelspot.domain.auth.controller;

import com.cow.fuelspot.domain.auth.dto.*;
import com.cow.fuelspot.global.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "인증 관련 API", description = "로그인, 토큰 재발급, 이메일 인증, 비밀번호 찾기 등 인증 관련 기능")
public interface AuthControllerDocs {

    @Operation(summary = "로그인", description = "이메일과 비밀번호를 입력받아 엑세스 토큰 및 리프레시 토큰을 발급합니다.")
    ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request);

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 이용하여 만료된 엑세스 토큰을 새로 발급받습니다.")
    ResponseEntity<ApiResponse<TokenDto>> reissue(@RequestBody @Valid TokenReissueRequest request);

    @Operation(summary = "로그아웃", description = "Redis에서 리프레시 토큰을 삭제하여 로그아웃 처리합니다.")
    ResponseEntity<ApiResponse<Void>> logout(@Parameter(hidden = true) UserDetails userDetails);

    @Operation(summary = "이메일 인증 코드 발송", description = "비밀번호 찾기를 위해 이메일로 6자리 인증 코드를 전송합니다.")
    ResponseEntity<ApiResponse<Void>> sendVerificationCode(@RequestBody @Valid EmailRequest request);

    @Operation(summary = "이메일 인증 코드 검증", description = "사용자가 입력한 인증 코드가 이메일로 전송된 코드와 일치하는지 확인합니다.")
    ResponseEntity<ApiResponse<Void>> verifyCode(@RequestBody @Valid EmailVerificationRequest request);

    @Operation(summary = "비밀번호 재설정 (로그인 전)", description = "이메일 인증을 완료한 후, 로그인하지 않은 상태에서 비밀번호를 새 비밀번호로 변경합니다.")
    ResponseEntity<ApiResponse<Void>> resetPassword(@RequestParam @Valid PasswordResetRequest request);
}
