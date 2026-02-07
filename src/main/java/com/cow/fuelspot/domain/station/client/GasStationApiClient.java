package com.cow.fuelspot.domain.station.client;

import com.cow.fuelspot.global.common.enums.FuelType;
import com.cow.fuelspot.domain.station.component.GasStationCacheManager;
import com.cow.fuelspot.domain.station.dto.enums.Sido;
import com.cow.fuelspot.domain.station.dto.opinet.*;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.cow.fuelspot.global.common.code.ErrorCode;
import com.cow.fuelspot.global.common.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
public class GasStationApiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final GasStationCacheManager cacheManager;

    private final String RADIUS_API_URL = "https://www.opinet.co.kr/api/aroundAll.do";
    private final String DETAIL_API_URL = "https://www.opinet.co.kr/api/detailById.do";
    private final String AVERAGE_API_URL ="https://www.opinet.co.kr/api/avgAllPrice.do";
    private final String AVERAGE_SIDO_API_URL ="https://www.opinet.co.kr/api/avgSidoPrice.do";

    @Value("${opinet.api-key}")
    private String apiKey;

    public GasStationApiClient(ObjectMapper objectMapper, GasStationCacheManager cacheManager) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
        this.cacheManager = cacheManager;
    }

    // 근처 주유소 조회
    public List<OpinetNearbyDto> getNearbyGasStations(NearbyRequest request, FuelType type) {
        // 캐시 키 생성
        String cacheKey = cacheManager.generateNearbyKey(
                request.getLat(),
                request.getLon(),
                request.getRadius(),
                type.getCode()
        );

        // 캐시 조회
        List<OpinetNearbyDto> cachedData = cacheManager.getNearbyCache(cacheKey);
        if (cachedData != null) {
            return cachedData;
        }

        // API 호출
        URI url = buildUri(RADIUS_API_URL, request.getLat(), request.getLon(), request.getRadius(), 1, type.getCode());
        OpinetResponse<OpinetNearbyDto> response = fetchAndParse(url, OpinetNearbyDto.class);
        List<OpinetNearbyDto> result = response.getOilList();

        // 데이터 검증
        if (result == null || result.isEmpty()) {
            throw new CustomException(ErrorCode.STATION_NO_CONTENT);
        }

        // 유효한 데이터만 캐시 저장
        cacheManager.putNearbyCache(cacheKey, result);

        return result;
    }

    // 필터 조회
    public List<OpinetNearbyDto> getStation(NearbyRequest request, FuelType type) {
        // 캐시 키 생성
        String cacheKey = cacheManager.generateNearbyKey(
                request.getLat(),
                request.getLon(),
                request.getRadius(),
                type.getCode()
        );

        // 캐시 조회
        List<OpinetNearbyDto> cachedData = cacheManager.getNearbyCache(cacheKey);
        if (cachedData != null) {
            return cachedData;
        }

        // API 호출
        URI url = buildUri(RADIUS_API_URL, request.getLat(), request.getLon(), request.getRadius(), 1, type.getCode());
        OpinetResponse<OpinetNearbyDto> response = fetchAndParse(url, OpinetNearbyDto.class);
        List<OpinetNearbyDto> result = response.getOilList();

        // 데이터 검증
        if (result == null || result.isEmpty()) {
            throw new CustomException(ErrorCode.STATION_NO_CONTENT);
        }

        // 유효한 데이터만 캐시 저장
        cacheManager.putNearbyCache(cacheKey, result);

        return result;
    }

    // 세부정보 조회
    public OpinetDetailDto getDetailGasStation(String id) {
        // 캐시 조회
        OpinetDetailDto cachedData = cacheManager.getDetailCache(id);
        if (cachedData != null) {
            return cachedData;
        }

        // API 호출
        URI url = UriComponentsBuilder.fromUriString(DETAIL_API_URL)
                .queryParam("code", apiKey)
                .queryParam("id", id)
                .queryParam("out", "json")
                .build()
                .toUri();

        OpinetResponse<OpinetDetailDto> response = fetchAndParse(url, OpinetDetailDto.class);
        List<OpinetDetailDto> list = response.getOilList();

        if (list == null || list.isEmpty()) {
            throw new CustomException(ErrorCode.STATION_NOT_FOUND);
        }

        OpinetDetailDto result = list.get(0);

        // 캐시 저장
        cacheManager.putDetailCache(id, result);

        return result;
    }

    // 평균 조회
    public List<OpinetAverageDto> getAverageGasStation() {
        // 캐시 조회
        List<OpinetAverageDto> cachedData = cacheManager.getAverageCache();
        if (cachedData != null) {
            return cachedData;
        }

        // API 호출
        URI url = UriComponentsBuilder.fromUriString(AVERAGE_API_URL)
                .queryParam("out","json")
                .queryParam("code", apiKey)
                .build()
                .toUri();
        OpinetResponse<OpinetAverageDto> response = fetchAndParse(url, OpinetAverageDto.class);
        List<OpinetAverageDto> result = response.getOilList();

        // 데이터 검증
        if (result == null || result.isEmpty()) {
            throw new CustomException(ErrorCode.STATION_NO_CONTENT);
        }

        // 유효한 데이터만 캐시 저장
        cacheManager.putAverageCache(result);

        return result;
    }

    // 시도별 조회
    public List<OpinetSidoAverageDto> getsidoAverageGasStation(Sido sido) {
        // 캐시 조회
        List<OpinetSidoAverageDto> cachedData = cacheManager.getSidoAverageCache(sido.getCode());
        if (cachedData != null) {
            return cachedData;
        }

        // API 호출
        URI url = UriComponentsBuilder.fromUriString(AVERAGE_SIDO_API_URL)
                .queryParam("out","json")
                .queryParam("code", apiKey)
                .queryParam("sido", sido.getCode())
                .build()
                .toUri();
        OpinetResponse<OpinetSidoAverageDto> response = fetchAndParse(url, OpinetSidoAverageDto.class);
        List<OpinetSidoAverageDto> result = response.getOilList();

        // 데이터 검증
        if (result == null || result.isEmpty()) {
            throw new CustomException(ErrorCode.STATION_NO_CONTENT);
        }

        // 유효한 데이터만 캐시 저장
        cacheManager.putSidoAverageCache(sido.getCode(), result);

        return result;
    }

    private URI buildUri(String baseUrl, Object x, Object y, Object radius, Object sort, Object prodcd) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("code", apiKey)
                .queryParam("x", x)
                .queryParam("y", y)
                .queryParam("radius", radius)
                .queryParam("sort", sort)
                .queryParam("prodcd", prodcd)
                .queryParam("out", "json")
                .build()
                .toUri();
    }

    private <T> OpinetResponse<T> fetchAndParse(URI url, Class<T> targetClass) {
        String responseBody;
        try {
            responseBody = restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            throw new CustomException(ErrorCode.STATION_API_COMMUNICATION_ERROR);
        }

        if (responseBody == null || responseBody.isBlank()) {
            throw new CustomException(ErrorCode.STATION_NO_CONTENT);
        }

        try {
            OpinetResponse<T> result = objectMapper.readValue(responseBody,
                    objectMapper.getTypeFactory().constructParametricType(OpinetResponse.class, targetClass));

            if (result == null || result.getOilList() == null) {
                throw new CustomException(ErrorCode.STATION_DATA_PARSE_ERROR);
            }
            return result;
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.STATION_SYSTEM_ERROR);
        }
    }
}