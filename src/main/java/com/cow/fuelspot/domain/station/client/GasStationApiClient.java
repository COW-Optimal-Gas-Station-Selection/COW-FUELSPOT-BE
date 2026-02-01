package com.cow.fuelspot.domain.station.client;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.opinet.*;
import com.cow.fuelspot.domain.station.dto.request.FilterRequest;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;


@Component
public class GasStationApiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String RADIUS_API_URL = "https://www.opinet.co.kr/api/aroundAll.do";
    private final String DETAIL_API_URL = "https://www.opinet.co.kr/api/detailById.do";
    private final String AVERAGE_API_URL ="https://www.opinet.co.kr/api/avgAllPrice.do";
    private final String AVERAGE_SIDO_API_URL ="https://www.opinet.co.kr/api/avgSidoPrice.do";

    @Value("${opinet.api-key}")
    private String apiKey;

    public GasStationApiClient(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }
    //근처 주유소 조회
    public List<OpinetNearbyDto> getNearbyGasStations(NearbyRequest request, FuelType type) {
        URI url = buildUri(RADIUS_API_URL, request.getLat(), request.getLon(), request.getRadius(), 1, type.getCode());
        OpinetResponse<OpinetNearbyDto> response = fetchAndParse(url, OpinetNearbyDto.class);
        return response.getOilList();
    }
    // 필터 조회
    public List<OpinetNearbyDto> getStation(FilterRequest request) {
        URI url = buildUri(RADIUS_API_URL, request.getLat(), request.getLon(), request.getRadius(), 1, request.getFuelType().getCode());
        OpinetResponse<OpinetNearbyDto> response = fetchAndParse(url, OpinetNearbyDto.class);
        return response.getOilList();
    }
    //세부정보 조회
    public OpinetDetailDto getDetailGasStation(String id) {
        URI url = UriComponentsBuilder.fromUriString(DETAIL_API_URL)
                .queryParam("code", apiKey)
                .queryParam("id", id)
                .queryParam("out", "json")
                .build()
                .toUri();
        OpinetResponse<OpinetDetailDto> response = fetchAndParse(url, OpinetDetailDto.class);
        List<OpinetDetailDto> list = fetchAndParse(url, OpinetDetailDto.class).getOilList();
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("해당 아이디로 조회된 주유소 상세 정보가 없습니다.");
        }
        return list.get(0);
    }
    //평균 조회
    public List<OpinetAverageDto> getAverageGasStation() {
        URI url = UriComponentsBuilder.fromUriString(AVERAGE_API_URL)
                .queryParam("out","json")
                .queryParam("code", apiKey)
                .build()
                .toUri();
        OpinetResponse<OpinetAverageDto> response = fetchAndParse(url, OpinetAverageDto.class);
        return response.getOilList();
    }
    //시도별 조회
    public List<OpinetSidoAverageDto> getsidoAverageGasStation(String sido) {
        URI url = UriComponentsBuilder.fromUriString(AVERAGE_API_URL)
                .queryParam("out","json")
                .queryParam("code", apiKey)
                .queryParam("sido", sido)
                .build()
                .toUri();
        OpinetResponse<OpinetSidoAverageDto> response = fetchAndParse(url, OpinetSidoAverageDto.class);
        return response.getOilList();
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
        } catch (org.springframework.web.client.RestClientException e) {
            throw new IllegalStateException("주유소 서버 통신에 실패했습니다.");
        }

        if (responseBody == null || responseBody.isBlank()) {
            throw new NoSuchElementException("조회된 정보가 없습니다.");
        }

        try {
            OpinetResponse<T> result = objectMapper.readValue(responseBody,
                    objectMapper.getTypeFactory().constructParametricType(OpinetResponse.class, targetClass));

            if (result == null || result.getOilList() == null) {
                throw new IllegalStateException("데이터 분석 중 오류가 발생했습니다.");
            }
            return result;
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("정보 처리 과정에서 시스템 오류가 발생했습니다.");
        }
    }
}