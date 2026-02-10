package com.cow.fuelspot.domain.car.service;

import com.cow.fuelspot.domain.car.dto.CarDetailDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarApiService {

    @Value("${car.api.key}")
    private String apiKey;

    @Value("${car.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // 1. 모델 검색 (필터링 적용)
    public List<CarDetailDto> searchCarsExternal(String keyword) {
        // 중복 제거를 위해 Map 사용 (Key: 깔끔한 모델명, Value: CarDto)
        // 같은 모델명이라면 첫 번째로 발견된 녀석의 연비를 대표값으로 씁니다. (혹은 평균을 낼 수도 있음)
        Map<String, CarDetailDto> uniqueCarMap = new HashMap<>();

        URI uri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("serviceKey", apiKey)
                .queryParam("perPage", 100) // 넉넉하게 가져옴
                .queryParam("page", 1)
                .queryParam("cond[차명::LIKE]", keyword)
                .build()
                .encode()
                .toUri();

        try {
            JsonNode root = restTemplate.getForObject(uri, JsonNode.class);
            JsonNode items = root.path("response").path("body").path("items");

            if (items.isArray()) {
                for (JsonNode item : items) {
                    String rawModelName = item.path("model_name").asText(); // 원본: "더 뉴 아반떼 (CN7) 1.6 가솔린"

                    // [핵심] 이름 청소하기
                    String cleanName = cleanModelName(rawModelName);

                    // 빈 문자열이거나, 이미 맵에 있으면 스킵 (중복 방지)
                    if (cleanName.isBlank() || uniqueCarMap.containsKey(cleanName)) {
                        continue;
                    }

                    CarDetailDto dto = CarDetailDto.builder()
                            .carName(cleanName) // 깔끔한 이름 넣기 ("아반떼")
                            .fuelEfficiency(item.path("efficiency").asDouble())
                            .fuelType(item.path("fuel_type").asText())
                            .build();

                    uniqueCarMap.put(cleanName, dto);
                }
            }
        } catch (Exception e) {
            log.error("API 연동 오류: {}", e.getMessage());
        }

        // Map의 값들만 뽑아서 리스트로 반환 (이름순 정렬 추천)
        return uniqueCarMap.values().stream()
                .sorted(Comparator.comparing(CarDetailDto::getCarName))
                .collect(Collectors.toList());
    }


    private String cleanModelName(String rawName) {
        if (rawName == null) return "";

        String name = rawName;

        // 1. 괄호와 그 안의 내용 제거: (CN7), [2023] 등
        name = name.replaceAll("\\(.*?\\)", "").replaceAll("\\[.*?\\]", "");

        // 2. 앞쪽 수식어 제거 (더 뉴, 올 뉴, 초저공해 등)
        name = name.replaceAll("(?i)(The New|All New|더 뉴|올 뉴|초저공해|봉고3|포터2)", "");

        // 3. 뒤쪽 스펙 제거 (배기량, 연료, 구동방식 등)
        // 예: 1.6, 2.0, 3.5, Turbo, Hybrid, LPi, 4WD, 2WD ...
        // 정규식 설명: 숫자.숫자, 숫자cc, 영어단어들 제거
        name = name.replaceAll("(?i)(\\d\\.\\d|\\d{3,4}cc|Turbo|Hybrid|LPi|GDi|eVGT|VGT|Gasoline|Diesel|LPG|2WD|4WD|A/T|M/T)", "");

        // 4. 특수문자 및 불필요한 공백 제거
        name = name.replaceAll("[^가-힣a-zA-Z0-9\\s]", ""); // 한글, 영어, 숫자, 공백 빼고 다 삭제
        name = name.trim(); // 앞뒤 공백 삭제
        name = name.replaceAll("\\s+", " "); // 중간에 여러 공백을 한 칸으로

        return name;
    }
}