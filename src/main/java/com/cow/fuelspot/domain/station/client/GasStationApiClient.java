package com.cow.fuelspot.domain.station.client;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetAverageDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.request.FilterRequest;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.cow.fuelspot.domain.station.dto.response.AverageStationResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Component
public class GasStationApiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String RADIUS_API_URL = "https://www.opinet.co.kr/api/aroundAll.do";
    private final String DETAIL_API_URL = "https://www.opinet.co.kr/api/detailById.do";
    private final String AVERAGE_API_URL ="https://www.opinet.co.kr/api/avgAllPrice.do";

    @Value("${opinet.api-key}")
    private String apiKey;

    public GasStationApiClient(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    //근처 주유소 조회
    public List<OpinetNearbyDto> getNearbyGasStations(NearbyRequest request, FuelType type) {
        URI url = buildUri(RADIUS_API_URL, request.getLat(), request.getLon(), request.getRadius(), 1, type.getCode());
        OpinetListResponse response = fetchAndParse(url, OpinetListResponse.class);

        if (response == null || response.getRESULT() == null || response.getRESULT().getOIL() == null) {
            return Collections.emptyList();
        }

        return response.getRESULT().getOIL();
    }

    public List<OpinetNearbyDto> getStation(FilterRequest request) {
        URI url = buildUri(RADIUS_API_URL, request.getLat(), request.getLon(), request.getRadius(), 1, request.getFuelType().getCode());
        OpinetListResponse response = fetchAndParse(url, OpinetListResponse.class);

        if (response == null || response.getRESULT() == null || response.getRESULT().getOIL() == null) {
            return Collections.emptyList();
        }

        return response.getRESULT().getOIL();
    }

    //상세 정보 조회
    public OpinetDetailDto getDetailGasStation(String id) {
        URI url = UriComponentsBuilder.fromUriString(DETAIL_API_URL)
                .queryParam("code", apiKey)
                .queryParam("id", id)
                .queryParam("out", "json")
                .build()
                .toUri();
        OpinetDetailResponse response = fetchAndParse(url, OpinetDetailResponse.class);
        if (response != null && response.getRESULT() != null && !response.getRESULT().getOIL().isEmpty()) {
            return response.getRESULT().getOIL().get(0);
        }
        return null;
    }
    //평균 조회
    public List<OpinetAverageDto> getAverageGasStation() {
        URI url = UriComponentsBuilder.fromUriString(AVERAGE_API_URL)
                .queryParam("out","json")
                .queryParam(  "code",apiKey)
                .build()
                .toUri();
        System.out.println("url: "+url);
        OpinetAverageResponse response = fetchAndParse(url, OpinetAverageResponse.class);
        System.out.println(List.of(response.getRESULT().getOIL().toArray(new OpinetAverageDto[0])));
        return List.of(response.getRESULT().getOIL().toArray(new OpinetAverageDto[0]));
    }

    //URL 빌더
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

    //html로 들어온 신호 변환
    private <T> T fetchAndParse(URI url, Class<T> responseType) {
        try {
            String htmlResponse = restTemplate.getForObject(url, String.class);
            return objectMapper.readValue(htmlResponse, responseType);
        } catch (Exception e) {
            throw new RuntimeException("API 요청 또는 파싱 실패: " + e.getMessage());
        }
    }

    //계층 표시용
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OpinetListResponse {
        @JsonProperty("RESULT")
        private ListResult RESULT;

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class ListResult {
            @JsonProperty("OIL")
            private List<OpinetNearbyDto> OIL;
        }
    }

    //계층 표시용
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OpinetDetailResponse {
        @JsonProperty("RESULT")
        private DetailResult RESULT;

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class DetailResult {
            @JsonProperty("OIL")
            private List<OpinetDetailDto> OIL;
        }
    }

    //계층 표시용
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OpinetAverageResponse {
        @JsonProperty("RESULT")
        private AverageResult RESULT;

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class AverageResult {
            @JsonProperty("OIL")
            private List<OpinetAverageDto> OIL;
        }
    }
}