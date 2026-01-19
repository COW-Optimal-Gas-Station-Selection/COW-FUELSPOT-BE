package com.cow.fuelspot.domain.station.client;

import com.cow.fuelspot.domain.station.dto.opinet.GasStationDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.List;

@Component
public class GasStationApiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String RADIUS_API_URL = "https://www.opinet.co.kr/api/aroundAll.do";
    private final String DETAIL_API_URL = "https://www.opinet.co.kr/api/detailById.do";

    public GasStationApiClient(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    public GasStationDto[] getNearbyGasStations(NearbyRequest request) {
        // 복잡한 변환 없이 request에 담긴 그대로를 파라미터로 사용합니다.
        URI url = buildUri(RADIUS_API_URL, request.getLat(), request.getLon(), request.getRadius(), 1, request.getFuelType());
        OpinetListResponse response = fetchAndParse(url, OpinetListResponse.class);
        return response.getRESULT().getOIL().toArray(new GasStationDto[0]);
    }

    public OpinetDetailDto getDetailGasStation(String id) {
        URI url = UriComponentsBuilder.fromUriString(DETAIL_API_URL)
                .queryParam("code", "F260118070")
                .queryParam("id", id)
                .queryParam("out", "json")
                .build()
                .toUri();

        OpinetDetailResponse response = fetchAndParse(url, OpinetDetailResponse.class);
        if (response.getRESULT() != null && !response.getRESULT().getOIL().isEmpty()) {
            return response.getRESULT().getOIL().get(0);
        }
        return null;
    }

    private URI buildUri(String baseUrl, Object x, Object y, Object radius, Object sort, Object prodcd) {
        // UriComponentsBuilder가 객체들의 toString()을 활용하도록 최소한의 구성만 유지합니다.
        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("code", "F260118070")
                .queryParam("x", x)
                .queryParam("y", y)
                .queryParam("radius", radius)
                .queryParam("sort", sort)
                .queryParam("prodcd", prodcd)
                .queryParam("out", "json")
                .build()
                .toUri();
    }

    private <T> T fetchAndParse(URI url, Class<T> responseType) {
        try {
            String htmlResponse = restTemplate.getForObject(url, String.class);
            // 파싱 전 로그는 유지하여 데이터의 정체성을 확인합니다.
            System.out.println(">>> [RAW Response]: " + htmlResponse);
            return objectMapper.readValue(htmlResponse, responseType);
        } catch (Exception e) {
            throw new RuntimeException("API 요청 또는 파싱 실패: " + e.getMessage());
        }
    }

    @Getter
    private static class OpinetListResponse {
        private ListResult RESULT;
        @Getter
        private static class ListResult {
            private List<GasStationDto> OIL;
        }
    }

    @Getter
    private static class OpinetDetailResponse {
        private DetailResult RESULT;
        @Getter
        private static class DetailResult {
            private List<OpinetDetailDto> OIL;
        }
    }
}