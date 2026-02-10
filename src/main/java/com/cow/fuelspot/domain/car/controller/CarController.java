package com.cow.fuelspot.domain.car.controller;

import com.cow.fuelspot.domain.car.dto.BrandDto;
import com.cow.fuelspot.domain.car.dto.CarResponse;
import com.cow.fuelspot.domain.car.service.CarService;
import com.cow.fuelspot.global.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@Tag(name = "Car", description = "자동차 정보 조회 API (브랜드/모델)")
public class CarController {

    private final CarService carService;

    @Operation(summary = "제조사(브랜드) 목록 조회", description = "BMW, 기아, 현대 등 브랜드 목록을 반환합니다.")
    @GetMapping("/brands")
    public ResponseEntity<ApiResponse<BrandDto>> getBrands() {
        BrandDto response = carService.getBrandList();
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "차량 모델 조회", description = "특정 브랜드(예: 현대)의 모델 목록(아반떼, 쏘나타...)을 반환합니다.")
    @GetMapping("/models")
    public ResponseEntity<ApiResponse<List<CarResponse>>> getModels(
            @Parameter(description = "브랜드 이름", example = "현대")
            @RequestParam String brand) {
        List<CarResponse> response = carService.getModels(brand);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}