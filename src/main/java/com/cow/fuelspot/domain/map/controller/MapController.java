package com.cow.fuelspot.domain.map.controller;

import com.cow.fuelspot.domain.map.dto.KakaoAddressResponse;
import com.cow.fuelspot.domain.map.dto.KakaoDirectionsResponse;
import com.cow.fuelspot.domain.map.dto.KakaoSearchResponse;
import com.cow.fuelspot.domain.map.service.KakaoMapService;
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
    public ResponseEntity<KakaoDirectionsResponse> getDirections(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(required = false) String waypoints
    ){
        KakaoDirectionsResponse response = kakaoMapService.getRoute(origin, destination, waypoints);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<KakaoSearchResponse> searchPlace(@RequestParam String keyword) {
        KakaoSearchResponse response = kakaoMapService.searchResponse(keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/address")
    public ResponseEntity<String> getAddress(
            @RequestParam String x,
            @RequestParam String y
    ) {
        String response = kakaoMapService.getAddressFromCoords(x, y);

        return ResponseEntity.ok(response);
    }
}
