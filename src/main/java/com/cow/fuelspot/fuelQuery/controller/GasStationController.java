package com.cow.fuelspot.fuelQuery.controller;


import com.cow.fuelspot.fuelQuery.dto.GasStationDto;
import com.cow.fuelspot.fuelQuery.dto.GasStationRequest;
import com.cow.fuelspot.fuelQuery.dto.StationDetailResponse;
import com.cow.fuelspot.fuelQuery.service.GasStationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gas-stations")
@RequiredArgsConstructor
public class GasStationController {

    private final GasStationService gasStationService;
    /**
     * [내 주변 주유소 조회] 및 [주유소 필터 조회]
     * URL: /api/gas-stations/nearby
     * Method: GET
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<GasStationDto>> getNearbyStations(@Valid GasStationRequest request) {
        List<GasStationDto> stations = gasStationService.getNearbyStations(request);
        return ResponseEntity.ok(stations);
    }

    /**
     * [주유소 상세 정보 조회]
     * URL: /api/gas-stations/{stationId}
     * Method: GET
     */
    @GetMapping("/{stationId}")
    public ResponseEntity<StationDetailResponse> getStationDetail(@PathVariable String stationId) {
        StationDetailResponse detail = gasStationService.getStationDetail(stationId);
        return ResponseEntity.ok(detail);
    }

}
