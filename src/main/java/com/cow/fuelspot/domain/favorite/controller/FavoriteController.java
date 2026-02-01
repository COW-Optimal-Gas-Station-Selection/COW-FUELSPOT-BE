package com.cow.fuelspot.domain.favorite.controller;

import com.cow.fuelspot.domain.favorite.dto.FavoriteRequest;
import com.cow.fuelspot.domain.favorite.dto.FavoriteResponse;
import com.cow.fuelspot.domain.favorite.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorite", description = "즐겨찾기 관련 API")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Operation(summary = "즐겨찾기 추가", description = "특정 주유소ID를 즐겨찾기에 추가합니다.")
    @PostMapping
    public ResponseEntity<FavoriteResponse> addFavorite(@RequestBody FavoriteRequest request, Authentication authentication) {
        String email = authentication.getName();
        FavoriteResponse response = favoriteService.addFavorite(email, request.getStationId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "즐겨찾기 취소", description = "특정 주유소의 즐겨찾기를 해제합니다.")
    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> removeFavorite(
            @Parameter(description = "오피넷 주유소 ID", example = "A0001234")
            @PathVariable String stationId,
            Authentication authentication) {
        String email = authentication.getName();
        favoriteService.removeFavorite(email, stationId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "주유소 찜 개수 조회", description = "해당 주유소를 몇 명이 찜했는지 확인합니다.")
    @GetMapping("/count/{stationId}")
    public ResponseEntity<Long> getFavoriteCount(@Parameter(description = "오피넷 주유소 ID", example = "A0001234") @PathVariable String stationId) {
        return ResponseEntity.ok(favoriteService.getFavoriteCount(stationId));
    }

    @Operation(summary = "내 즐겨찾기 목록 조회", description = "현재 로그인한 사용자의 즐겨찾기 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getMyFavorites(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(favoriteService.getMemberFavorites(email));
    }
}
