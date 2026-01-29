package com.cow.fuelspot.domain.member.controller;

import com.cow.fuelspot.domain.member.service.MemberService;
import com.cow.fuelspot.domain.member.dto.MemberSignupRequest;
import com.cow.fuelspot.domain.member.dto.PasswordChangeRequest;
import com.cow.fuelspot.domain.member.dto.MemberInfoResponse;
import com.cow.fuelspot.domain.member.dto.MemberUpdateRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.HashMap;
import java.util.Map;

// 회원 컨트롤러
// 회원가입, 내 정보 조회 등 회원과 관련된 요청 처리
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor // 의존성 주입
public class MemberController {

    private final MemberService memberService;

    // 회원가입 API
    // POST /api/members
    @PostMapping
    public ResponseEntity<Map<String, Object>> signup(@RequestBody @Valid MemberSignupRequest request) {

        // 서비스에게 회원가입 위임 (중복 검사 -> 암호화 -> DB 저장)
        memberService.signup(request);

        // 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", true);
        response.put("message", "회원가입이 완료되었습니다");

        // 201 Created 상태 코드와 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 내 정보 조회 API
    // GET /api/members/me
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        // @AuthenticationPrincipal: SecurityContextHolder에 있는 현재 접속한 사람 정보 (UserDetails 객체) 주입

        MemberInfoResponse response = memberService.getMyInfo(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }

    // 내 정보 수정 API
    // PATCH /api/members/me
    @PatchMapping("/me")
    public ResponseEntity<MemberInfoResponse> updateMyInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestBody @Valid MemberUpdateRequest request) {

        MemberInfoResponse response = memberService.updateMyInfo(userDetails.getUsername(), request);

        return ResponseEntity.ok(response);
    }

    // 회원 탈퇴 API
    // DELETE /api/members/me
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(@AuthenticationPrincipal UserDetails userDetails) {
        memberService.deleteMyAccount(userDetails.getUsername());

        return ResponseEntity.noContent().build();
    }

    // 비밀번호 변경 API
    // PATCH /api/members/password
    @PatchMapping("/password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PasswordChangeRequest request) {

        memberService.changePassword(userDetails.getUsername(), request);

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
