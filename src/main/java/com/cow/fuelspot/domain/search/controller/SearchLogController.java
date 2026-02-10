package com.cow.fuelspot.domain.search.controller;

import com.cow.fuelspot.domain.search.service.SearchLogService;
import com.cow.fuelspot.global.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/search/log")
@RequiredArgsConstructor
@Tag(name = "Search Log", description = "검색어 기록 관련 API")
public class SearchLogController {

    private final SearchLogService searchLogService;

    @Operation(summary = "최근 검색어 조회", description = "로그인한 사용자의 최근 검색어 5개를 가져옵니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Set<String>>> getRecentKeywords(Authentication authentication) {
        String email = authentication.getName();
        Set<String> keywords = searchLogService.getRecentKeywords(email);
        return ResponseEntity.ok(ApiResponse.onSuccess(keywords));
    }

    @Operation(summary = "최근 검색어 삭제", description = "특정 검색어 하나를 기록에서 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteKeyword(
            @RequestParam String keyword,
            Authentication authentication) {
        String email = authentication.getName();
        searchLogService.deleteKeyword(email, keyword);
        return ResponseEntity.ok(ApiResponse.onSuccess());
    }

    @Operation(summary = "최근 검색어 전체 삭제", description = "사용자의 모든 검색 기록을 초기화합니다.")
    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<Void>> deleteAllKeywords(Authentication authentication) {
        String email = authentication.getName();
        searchLogService.deleteAllKeywords(email);
        return ResponseEntity.ok(ApiResponse.onSuccess());
    }
}
