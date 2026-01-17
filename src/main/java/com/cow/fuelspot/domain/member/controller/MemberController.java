package com.cow.fuelspot.domain.member.controller;

import com.cow.fuelspot.domain.member.service.MemberService;
import com.cow.fuelspot.domain.member.dto.MemberSignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 회원 컨트롤러 (요청의 내용을 검사하고, 서비스에 전달)
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor // 의존성 주입
public class MemberController {

    private final MemberService memberService;

    // 회원가입 API
    // POST /api/members
    @PostMapping
    public ResponseEntity<String> signup(@RequestBody @Valid MemberSignupRequest request) {
        // @RequestBody: JSON -> 자바 객체로 변환
        // @Valid: TO의 조건 검사

        // 서비스 호출
        memberService.signup(request);

        // 응답 반환
        // HttpStatus.CREATED (201)
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }
}
