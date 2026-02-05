package com.cow.fuelspot.domain.map.controller;

import com.cow.fuelspot.domain.map.dto.KakaoDirectionsResponse;
import com.cow.fuelspot.domain.map.dto.KakaoSearchResponse;
import com.cow.fuelspot.domain.map.dto.KakaoTranscoordResponse;
import com.cow.fuelspot.global.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "지도 관련 API", description = "지도 검색, 경로 탐색, 좌표 변환 등 카카오 맵 관련 기능")
public interface MapControllerDocs {

    @Operation(summary = "경로 탐색", description = "출발지와 도착지 좌표를 입력받아 차량 이동 경로 반환합니다.")
    ResponseEntity<ApiResponse<KakaoDirectionsResponse>> findRoute(@RequestParam String origin, @RequestParam String destination);

    @Operation(summary = "장소 검색 (키워드)", description = "키워드로 장소를 검색합니다.")
    ResponseEntity<ApiResponse<KakaoSearchResponse>> searchPlace(@RequestParam String keyword);

    @Operation(summary = "좌표로 주소 변환", description = "WGS84 좌표(경도x, 위도 y)를 입력받아 지번/도로명 주소를 반환합니다.")
    ResponseEntity<ApiResponse<String>> convertCoordsToAddress(@RequestParam String x, @RequestParam String y);

    @Operation(summary = "좌표계 변환 (WGS84 -> KTM)", description = "WGS84 좌표를 KTM 좌표로 변환합니다.")
    ResponseEntity<ApiResponse<KakaoTranscoordResponse>> convertWGS84ToKTM(@RequestParam String x, @RequestParam String y);

    @Operation(summary = "좌표계 변환 (KTM -> WGS84)", description = "KTM 좌표를 WGS84 좌표로 변환합니다.")
    ResponseEntity<ApiResponse<KakaoTranscoordResponse>> convertKTMToWGS84(@RequestParam String x, @RequestParam String y);
}
