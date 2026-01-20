package com.cow.fuelspot.domain.member.controller;

import com.cow.fuelspot.domain.member.service.MemberService;
import com.cow.fuelspot.domain.member.dto.LoginRequest;
import com.cow.fuelspot.domain.member.dto.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 인증 컨트롤러 (로그인 등 인증 관련 API 전담)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    // 로그인 API
    // POST /api/auth/login
    // @param request 사용자가 입력한 이메일, 비밀번호
    // @return 로그인 성공 시 토큰과 사용자 정보 반환
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {

        // 서비스에 로그인 처리 (DB 조회 -> 비번 검사 -> 토큰 생성)
        LoginResponse response = memberService.login(request);

        // 200 OK 상태 코드와 응답 반환
        return ResponseEntity.ok(response);
    }
}
