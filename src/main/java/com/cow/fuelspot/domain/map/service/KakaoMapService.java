package com.cow.fuelspot.domain.map.service;

import com.cow.fuelspot.domain.map.dto.KakaoAddressResponse;
import com.cow.fuelspot.domain.map.dto.KakaoDirectionsResponse;
import com.cow.fuelspot.domain.map.dto.KakaoSearchResponse;
import com.cow.fuelspot.global.common.code.ErrorCode;
import com.cow.fuelspot.global.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoMapService {

    private final RestTemplate restTemplate;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    private static final String KAKAO_DIRECTIONS_URL = "https://apis-navi.kakaomobility.com/v1/directions";
    private static final String KAKAO_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
    private static final String KAKAO_COORD_URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json";

    // 길찾기
    public KakaoDirectionsResponse getRoute(String origin, String destination) {

        URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_DIRECTIONS_URL)
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("priority", "RECOMMEND")
                .queryParam("summary", false)
                .build().encode().toUri();

        return callKakaoApi(uri, KakaoDirectionsResponse.class);
    }

    // 장소 검색
    public KakaoSearchResponse searchResponse(String keyword) {
        URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_SEARCH_URL)
                .queryParam("query", keyword)
                .queryParam("size", 10)
                .build().encode().toUri();

        return callKakaoApi(uri, KakaoSearchResponse.class);
    }

    // 주소 변환
    public String getAddressFromCoords(String x, String y) {
        URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_COORD_URL)
                .queryParam("x", x)
                .queryParam("y", y)
                .build().encode().toUri();

        KakaoAddressResponse response = callKakaoApi(uri, KakaoAddressResponse.class);

        if (response != null && !response.getDocuments().isEmpty()) {
            KakaoAddressResponse.Document doc = response.getDocuments().get(0);

            if (doc.getRoadAddress() != null) {
                String fullAddress = doc.getRoadAddress().getAddressName();
                String buildingName = doc.getRoadAddress().getBuildingName();

                if (buildingName != null && !buildingName.isEmpty()) {
                    fullAddress += " (" + buildingName + ")";
                }
                return fullAddress;
            } else if (doc.getAddress() != null) {
                return doc.getAddress().getAddressName();
            }
        }
        // 못 찾으면 null 반환
        return null;
    }

    // 공통 호출 메서드
    private <T> T callKakaoApi(URI uri, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " +  kakaoRestApiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try{
            log.info("[Kakao API] Request: {}", uri);
            ResponseEntity<T> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    responseType
            );
            return response.getBody();
        } catch (RestClientException e) {
            log.error("[Kakao API] Error: {}", e.getMessage());
            throw new CustomException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }
}
