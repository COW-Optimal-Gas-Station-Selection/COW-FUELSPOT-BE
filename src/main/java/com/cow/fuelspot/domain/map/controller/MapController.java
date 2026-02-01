package com.cow.fuelspot.domain.map.controller;

import com.cow.fuelspot.domain.map.dto.KakaoAddressResponse;
import com.cow.fuelspot.domain.map.dto.KakaoDirectionsResponse;
import com.cow.fuelspot.domain.map.dto.KakaoSearchResponse;
import com.cow.fuelspot.domain.map.service.KakaoMapService;
import com.cow.fuelspot.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final KakaoMapService kakaoMapService;

    @GetMapping("/direction")
    public ResponseEntity<ApiResponse<KakaoDirectionsResponse>> getDirections(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(required = false) String waypoints
    ){
        KakaoDirectionsResponse response = kakaoMapService.getRoute(origin, destination);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<KakaoSearchResponse>> searchPlace(@RequestParam String keyword) {
        KakaoSearchResponse response = kakaoMapService.searchResponse(keyword);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @GetMapping("/address")
    public ResponseEntity<ApiResponse<String>> getAddress(
            @RequestParam String x,
            @RequestParam String y
    ) {
        String response = kakaoMapService.getAddressFromCoords(x, y);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
