package com.cow.fuelspot.domain.search.controller;

import com.cow.fuelspot.domain.search.service.SearchLogService;
import com.cow.fuelspot.global.common.code.ErrorCode;
import com.cow.fuelspot.global.common.dto.ApiResponse;
import com.cow.fuelspot.global.common.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/search/log")
@RequiredArgsConstructor
@Tag(name = "Search Log", description = "검색어 기록 관련 API")
public class SearchLogController {

    private final SearchLogService searchLogService;

    private void validateUser(UserDetails userDetails) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }
    @Operation(summary = "검색어 저장", description = "사용자가 선택한 장소명을 저장합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveSearchLog(
            @RequestParam String keyword,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        validateUser(userDetails);
        searchLogService.saveSearchKeyword(userDetails.getUsername(), keyword);
        return ResponseEntity.ok(ApiResponse.onSuccess());
    }

    @Operation(summary = "최근 검색어 저장", description = "로그인한 사용자의 최근 검색어 5개를 가져옵니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Set<String>>> getRecentKeywords(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        validateUser(userDetails);
        Set<String> keywords = searchLogService.getRecentKeywords(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.onSuccess(keywords));
    }

    @Operation(summary = "최근 검색어 삭제", description = "특정 검색어 하나를 기록에서 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteKeyword(
            @RequestParam String keyword,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        validateUser(userDetails);
        searchLogService.deleteKeyword(userDetails.getUsername(), keyword);
        return ResponseEntity.ok(ApiResponse.onSuccess());
    }

    @Operation(summary = "최근 검색어 전체 삭제", description = "사용자의 모든 검색 기록을 초기화합니다.")
    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<Void>> deleteAllKeywords(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        validateUser(userDetails);
        searchLogService.deleteAllKeywords(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.onSuccess());
    }
}
