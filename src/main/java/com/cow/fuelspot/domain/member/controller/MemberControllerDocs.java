package com.cow.fuelspot.domain.member.controller;

import com.cow.fuelspot.domain.member.dto.MemberInfoResponse;
import com.cow.fuelspot.domain.member.dto.MemberSignupRequest;
import com.cow.fuelspot.domain.member.dto.MemberUpdateRequest;
import com.cow.fuelspot.domain.member.dto.PasswordChangeRequest;
import com.cow.fuelspot.global.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Member", description = "회원 관련 API")
public interface MemberControllerDocs {

    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임, 선호 유종 등을 입력받아 신규 회원을 등록합니다.")
    ResponseEntity<ApiResponse<Long>> signup(@RequestBody @Valid MemberSignupRequest request);

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 프로필 정보(닉네임, 유종, 검색 반경 등)를 조회합니다.")
    ResponseEntity<ApiResponse<MemberInfoResponse>> getMyInfo(@Parameter(hidden = true) UserDetails userDetails);

    @Operation(summary = "내 정보 수정", description = "로그인한 사용자의 닉네임, 선호 유종, 검색 반경을 수정합니다.")
    ResponseEntity<ApiResponse<MemberInfoResponse>> updateMyInfo(
            @Parameter(hidden = true) UserDetails userDetails,
            @RequestBody @Valid MemberUpdateRequest request);

    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자의 계정을 영구적으로 삭제합니다.")
    ResponseEntity<ApiResponse<Void>> deleteMyAccount(@Parameter(hidden = true) UserDetails userDetails);

    @Operation(summary = "비밀번호 변경 (로그인 후)", description = "로그인한 상태에서 기존 비밀번호를 확인하고 새 비밀번호로 변경합니다.")
    ResponseEntity<ApiResponse<Void>> changePassword(@Parameter(hidden = true) UserDetails userDetails, @RequestBody @Valid PasswordChangeRequest request);
}
