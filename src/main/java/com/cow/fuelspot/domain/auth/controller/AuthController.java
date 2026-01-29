package com.cow.fuelspot.domain.auth.controller;

import com.cow.fuelspot.domain.auth.service.AuthService; // [변경]
import com.cow.fuelspot.domain.auth.dto.TokenDto;
import com.cow.fuelspot.domain.auth.dto.LoginRequest;
import com.cow.fuelspot.domain.auth.dto.LoginResponse;
import com.cow.fuelspot.domain.auth.dto.TokenReissueRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cow.fuelspot.domain.auth.dto.EmailRequest;
import com.cow.fuelspot.domain.auth.dto.EmailVerificationRequest;
import com.cow.fuelspot.domain.auth.dto.PasswordResetRequest;

// 인증 컨트롤러 (로그인 등 인증 관련 API 전담)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 로그인 API
    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // 토큰 재발급 API
    // POST /api/auth/reissue
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenReissueRequest request) {
        return ResponseEntity.ok(authService.reissue(request));
    }

    // 로그아웃 API
    // POST /api/auth/logout
    // 프론트엔드에서 토큰 삭제
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails) {
        // @AuthenticationPrincipal: 현재 로그인한 사용자의 정보를 가져옴
        authService.logout(userDetails.getUsername()); // 서비스 호출해서 DB 삭제
        return ResponseEntity.ok("로그아웃이 완료되었습니다.");
    }

    // 인증 코드 발송 API
    // POST /api/auth/email/send
    @PostMapping("/email/send")
    public ResponseEntity<String> sendVerificationCode(@RequestBody @Valid EmailRequest request) {
        authService.sendVerificationCode(request);
        return ResponseEntity.ok("인증 코드가 발송되었습니다.");
    }

    // 인증 코드 검증 API
    // POST /api/auth/email/verify
    @PostMapping("/email/verify")
    public ResponseEntity<String> verifyCode(@RequestBody @Valid EmailVerificationRequest request) {
        authService.verifyCode(request);
        return ResponseEntity.ok("인증에 성공했습니다.");
    }

    // 비밀번호 재설정 API
    // POST /api/auth/password/resset
    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
