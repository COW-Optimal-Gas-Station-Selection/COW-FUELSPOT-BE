package com.cow.fuelspot.domain.auth.controller;

import com.cow.fuelspot.domain.auth.service.AuthService;
import com.cow.fuelspot.domain.auth.service.EmailService;
import com.cow.fuelspot.domain.auth.dto.*;
import com.cow.fuelspot.global.common.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 인증 컨트롤러 (로그인 등 인증 관련 API 전담)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    // 로그인 API
    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    // 토큰 재발급 API
    // POST /api/auth/reissue
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenDto>> reissue(@RequestBody @Valid TokenReissueRequest request) {
        TokenDto response = authService.reissue(request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    // 로그아웃 API
    // POST /api/auth/logout
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal UserDetails userDetails) {
        // @AuthenticationPrincipal: 현재 로그인한 사용자의 정보를 가져옴
        authService.logout(userDetails.getUsername()); // 서비스 호출해서 DB 삭제
        return ResponseEntity.ok(ApiResponse.onSuccess());
    }

    // 인증 코드 발송 API
    // POST /api/auth/email/send
    @PostMapping("/email/send")
    public ResponseEntity<ApiResponse<Void>> sendVerificationCode(@RequestBody @Valid EmailRequest request) {
        emailService.sendVerificationCode(request);
        return ResponseEntity.ok(ApiResponse.onSuccess());
    }

    // 인증 코드 검증 API
    // POST /api/auth/email/verify
    @PostMapping("/email/verify")
    public ResponseEntity<ApiResponse<Void>> verifyCode(@RequestBody @Valid EmailVerificationRequest request) {
        emailService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(ApiResponse.onSuccess());
    }

    // 비밀번호 찾기 API
    // POST /api/auth/password/resset
    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.onSuccess());
    }
}
