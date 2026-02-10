package com.cow.fuelspot.domain.car.service;

import com.cow.fuelspot.domain.car.dto.BrandDto;
import com.cow.fuelspot.domain.car.dto.CachedCar;
import com.cow.fuelspot.domain.car.dto.CarResponse;
import com.cow.fuelspot.domain.car.enums.Brand;
import com.cow.fuelspot.global.common.enums.FuelType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {

    private final List<CachedCar> carList = new ArrayList<>();

    @PostConstruct
    public void loadCars() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new ClassPathResource("cars.csv").getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }

                String[] data = line.split(",");
                if (data.length < 4) continue;

                String brand = data[0].trim();
                String modelName = data[1].trim();
                String fuelStr = data[2].trim(); // 예: "가솔린", "하이브리드"
                double efficiency = Double.parseDouble(data[3].trim());

                FuelType fuelType = FuelType.fromCsvName(fuelStr);

                if (fuelType == null) {
                    continue;
                }

                carList.add(CachedCar.builder()
                        .brand(brand)
                        .modelName(modelName)
                        .fuelType(fuelType)
                        .fuelEfficiency(efficiency)
                        .build());
            }
            log.info("✅ 자동차 데이터 로딩 완료: 총 {}대", carList.size());

        } catch (Exception e) {
            log.error("❌ CSV 로딩 실패", e);
        }
    }

    public BrandDto getBrandList() {
        List<String> brands = Arrays.stream(Brand.values())
                .map(Brand::getName)
                .collect(Collectors.toList());
        return new BrandDto(brands);
    }

    public List<CarResponse> getModels(String brandName) {
        return carList.stream()
                .filter(car -> car.getBrand().equals(brandName))
                .map(CachedCar::getModelName)
                .distinct()
                .sorted()
                .map(name -> CarResponse.builder().modelName(name).build())
                .collect(Collectors.toList());
    }

    public CachedCar findCar(String brand, String modelName, FuelType userFuelType) {
        return carList.stream()
                .filter(car -> car.getBrand().equals(brand))
                .filter(car -> car.getModelName().equals(modelName))
                .filter(car -> car.getFuelType() == userFuelType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("조건에 맞는 차량을 찾을 수 없습니다."));
    }
}