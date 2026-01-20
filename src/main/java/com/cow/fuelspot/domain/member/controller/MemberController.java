package com.cow.fuelspot.domain.member.controller;

import com.cow.fuelspot.domain.member.service.MemberService;
import com.cow.fuelspot.domain.member.dto.MemberSignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 회원 컨트롤러
// 회원가입, 내 정보 조회 등 회원과 관련된 요청 처리
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor // 의존성 주입
public class MemberController {

    private final MemberService memberService;

    // 회원가입 API
    // POST /api/members
    // @param request 회원가입 정보 (이메일, 비번, 닉네임 등)
    // @return 성공 시 201 Created 상태 코드와 메시지 반환
    @PostMapping
    public ResponseEntity<String> signup(@RequestBody @Valid MemberSignupRequest request) {
        // @RequestBody: JSON -> 자바 객체로 변환
        // @Valid: DTO의 조건 검사

        // 서비스에게 회원가입 위임 (중복 검사 -> 암호화 -> DB 저장)
        memberService.signup(request);

        // 201 Created 상태 코드와 성공 메세지 반환
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }
    
    @GetMapping("/test")
    public String test() {
        return "토큰 인증 성공";
    }
}
