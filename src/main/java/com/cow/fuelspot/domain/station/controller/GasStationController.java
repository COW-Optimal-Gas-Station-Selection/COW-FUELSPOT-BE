package com.cow.fuelspot.domain.station.controller;

import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.request.FilterRequest;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.cow.fuelspot.domain.station.dto.response.AverageStationResponse;
import com.cow.fuelspot.domain.station.dto.response.DetailResponse;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;
import com.cow.fuelspot.domain.station.service.OpinetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gas-stations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GasStationController {
    private final OpinetService opinetService;
    //근처 주유소 조회
    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyResponse>> getNearbyStations(@Valid NearbyRequest request) {
        List<NearbyResponse> stations = opinetService.getNearbyGasStations(request);
        return ResponseEntity.ok(stations);
    }
    //주유소 상세 정보 조회
    @GetMapping("/{stationId}")
    public ResponseEntity<DetailResponse> getStationDetail(@PathVariable String stationId) {
        OpinetDetailDto dto = opinetService.getDetailGasStation(stationId);
        DetailResponse detail = DetailResponse.from(dto);
        return ResponseEntity.ok(detail);
    }
    //필터 조회
    @GetMapping("/filter")
    public ResponseEntity<List<NearbyResponse>> getStationDetail(@Valid FilterRequest request) {
        List<NearbyResponse> stations = opinetService.getFilteredStations(request);
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/average")
    public ResponseEntity<AverageStationResponse> getStationAverage() {
        AverageStationResponse stations = opinetService.getAverageStation();
        return ResponseEntity.ok(stations);
    }
}
