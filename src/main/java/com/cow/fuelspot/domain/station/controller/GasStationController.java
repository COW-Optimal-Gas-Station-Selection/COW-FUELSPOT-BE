package com.cow.fuelspot.domain.station.controller;

import com.cow.fuelspot.domain.station.dto.enums.Sido;
import com.cow.fuelspot.domain.station.dto.request.FilterRequest;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.cow.fuelspot.domain.station.dto.response.AverageStationResponse;
import com.cow.fuelspot.domain.station.dto.response.DetailResponse;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;
import com.cow.fuelspot.domain.station.service.OpinetService;
import com.cow.fuelspot.global.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gas-stations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "GasStation", description = "주유소 관련 API")
public class GasStationController {

    private final OpinetService opinetService;

    @Operation(summary = "근처 주유소 조회", description = "위치 기반으로 근처 주유소를 조회합니다.")
    @GetMapping("/nearby")
    public ApiResponse<List<NearbyResponse>> getNearbyStations(@Valid NearbyRequest request,
                                                               Authentication authentication) {
        List<NearbyResponse> stations = opinetService.getNearbyGasStations(request, authentication);
        return ApiResponse.onSuccess(stations);
    }

    @Operation(summary = "주유소 상세 정보 조회", description = "특정 주유소의 상세 정보를 조회합니다.")
    @GetMapping("/{stationId}")
    public ApiResponse<DetailResponse> getStationDetail(
            @Parameter(description = "오피넷 주유소 ID", example = "A0009912")
            @PathVariable String stationId) {
        DetailResponse detail = opinetService.getDetailGasStation(stationId);
        return ApiResponse.onSuccess(detail);
    }

    @Operation(summary = "필터 조회", description = "필터 조건에 맞는 주유소를 조회합니다.")
    @GetMapping("/filter")
    public ApiResponse<List<NearbyResponse>> getStationDetail(@Valid FilterRequest request,
                                                              Authentication authentication) {
        List<NearbyResponse> stations = opinetService.getFilteredStations(request, authentication);
        return ApiResponse.onSuccess(stations);
    }

    @Operation(summary = "전국 평균 가격 조회", description = "전국 주유소의 평균 가격을 조회합니다.")
    @GetMapping("/average")
    public ApiResponse<AverageStationResponse> getStationAverage() {
        AverageStationResponse stations = opinetService.getAverageStation();
        return ApiResponse.onSuccess(stations);
    }

    @Operation(summary = "시도별 평균 가격 조회", description = "특정 시도의 평균 가격을 조회합니다.")
    @GetMapping("/average/sido")
    public ApiResponse<AverageStationResponse> getStationSidoAverage(
            @Parameter(description = "시도명", example = "SEOUL")
            @RequestParam @NotNull(message = "시도는 필수 요소 입니다.") Sido sido) {

        AverageStationResponse stations = opinetService.getSidoAverageStation(sido);
        return ApiResponse.onSuccess(stations);
    }
}