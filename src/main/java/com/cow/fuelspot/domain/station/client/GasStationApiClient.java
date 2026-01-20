package com.cow.fuelspot.domain.station.client;

import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public OpinetNearbyDto[] getNearbyGasStations(NearbyRequest request) {
        URI url = buildUri(RADIUS_API_URL, request.getLat(), request.getLon(), request.getRadius(), 1, request.getFuelType());
        OpinetListResponse response = fetchAndParse(url, OpinetListResponse.class);

        if (response == null || response.getRESULT() == null || response.getRESULT().getOIL() == null) {
            return new OpinetNearbyDto[0];
        }
        return response.getRESULT().getOIL().toArray(new OpinetNearbyDto[0]);
    }

    public OpinetDetailDto getDetailGasStation(String id) {
        URI url = UriComponentsBuilder.fromUriString(DETAIL_API_URL)
                .queryParam("code", "F260118070")
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

    private URI buildUri(String baseUrl, Object x, Object y, Object radius, Object sort, Object prodcd) {
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
            System.out.println(">>> [RAW Response]: " + htmlResponse);
            return objectMapper.readValue(htmlResponse, responseType);
        } catch (Exception e) {
            throw new RuntimeException("API 요청 또는 파싱 실패: " + e.getMessage());
        }
    }

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
}