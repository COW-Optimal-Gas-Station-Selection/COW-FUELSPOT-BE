package com.cow.fuelspot.domain.map.controller;

import com.cow.fuelspot.domain.map.dto.KakaoDirectionsResponse;
import com.cow.fuelspot.domain.map.dto.KakaoSearchResponse;
import com.cow.fuelspot.domain.map.dto.KakaoTranscoordResponse;
import com.cow.fuelspot.domain.map.service.KakaoMapService;
import com.cow.fuelspot.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController implements MapControllerDocs{

    private final KakaoMapService kakaoMapService;

    @Override
    @GetMapping("/direction")
    public ResponseEntity<ApiResponse<KakaoDirectionsResponse>> findRoute(
            @RequestParam String origin,
            @RequestParam String destination
    ){
        KakaoDirectionsResponse response = kakaoMapService.findRoute(origin, destination);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<KakaoSearchResponse>> searchPlace(
            @RequestParam String keyword,
            Authentication authentication
    ) {
        String email = null;
        if (authentication != null && authentication.isAuthenticated()) {
            email = authentication.getName();
        }

        KakaoSearchResponse response = kakaoMapService.searchPlaces(email, keyword);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Override
    @GetMapping("/address")
    public ResponseEntity<ApiResponse<String>> convertCoordsToAddress(
            @RequestParam String x,
            @RequestParam String y
    ) {
        String response = kakaoMapService.convertCoordsToAddress(x, y);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Override
    @GetMapping("/convert/wgs84-to-ktm")
    public ResponseEntity<ApiResponse<KakaoTranscoordResponse>> convertWGS84ToKTM(
            @RequestParam String x,
            @RequestParam String y
    ) {
        KakaoTranscoordResponse response = kakaoMapService.convertWGS84ToKTM(x, y);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Override
    @GetMapping("/convert/ktm-to-wgs84")
    public ResponseEntity<ApiResponse<KakaoTranscoordResponse>> convertKTMToWGS84(
            @RequestParam String x,
            @RequestParam String y
    ) {
        KakaoTranscoordResponse response = kakaoMapService.convertKTMToWGS84(x, y);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
