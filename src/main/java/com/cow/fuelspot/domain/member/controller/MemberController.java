package com.cow.fuelspot.domain.member.controller;

import com.cow.fuelspot.domain.member.service.MemberService;
import com.cow.fuelspot.domain.member.dto.MemberSignupRequest;
import com.cow.fuelspot.domain.member.dto.PasswordChangeRequest;
import com.cow.fuelspot.domain.member.dto.MemberInfoResponse;
import com.cow.fuelspot.domain.member.dto.MemberUpdateRequest;
import com.cow.fuelspot.global.common.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

// 회원 컨트롤러
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor // 의존성 주입
public class MemberController implements MemberControllerDocs {

    private final MemberService memberService;

    // 회원가입 API
    // POST /api/members
    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> signup(@RequestBody @Valid MemberSignupRequest request) {
        Long memberId = memberService.signup(request);

        // 201 Created 상태 코드와 응답 반환
        return ResponseEntity.ok(ApiResponse.onSuccess(memberId));
    }

    // 내 정보 조회 API
    // GET /api/members/me
    @Override
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberInfoResponse>> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        // @AuthenticationPrincipal: SecurityContextHolder에 있는 현재 접속한 사람 정보 (UserDetails 객체) 주입
        MemberInfoResponse response = memberService.getMyInfo(userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    // 내 정보 수정 API
    // PATCH /api/members/me
    @Override
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<MemberInfoResponse>> updateMyInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid MemberUpdateRequest request) {

        MemberInfoResponse response = memberService.updateMyInfo(userDetails.getUsername(), request);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    // 회원 탈퇴 API
    // DELETE /api/members/me
    @Override
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteMyAccount(@AuthenticationPrincipal UserDetails userDetails) {
        memberService.deleteMyAccount(userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.onSuccess());
    }

    // 비밀번호 변경 API
    // PATCH /api/members/password
    @Override
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PasswordChangeRequest request) {

        memberService.changePassword(userDetails.getUsername(), request);

        return ResponseEntity.ok(ApiResponse.onSuccess());
    }
}
