package com.cow.fuelspot.domain.station.controller;

import com.cow.fuelspot.domain.station.dto.opinet.GasStationDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
//import com.cow.fuelspot.domain.station.dto.request.FilterGasStationRequest;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
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
public class GasStationController {

    private final OpinetService opinetService;

    @GetMapping("/nearby")
    public ResponseEntity<List<GasStationDto>> getNearbyStations(@Valid NearbyRequest request) {
        List<GasStationDto> stations = opinetService.getNearbyGasStations(request);
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<DetailResponse> getStationDetail(@PathVariable String stationId) {
        OpinetDetailDto dto = opinetService.getDetailGasStation(stationId);
        DetailResponse detail = DetailResponse.from(dto);
        return ResponseEntity.ok(detail);
    }
}
