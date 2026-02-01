package com.cow.fuelspot.domain.map.service;

import com.cow.fuelspot.domain.map.dto.KakaoAddressResponse;
import com.cow.fuelspot.domain.map.dto.KakaoDirectionsResponse;
import com.cow.fuelspot.domain.map.dto.KakaoSearchResponse;
import com.cow.fuelspot.domain.map.dto.KakaoTranscoordResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
    private static final String KAKAO_TRANSCOORD_URL = "https://dapi.kakao.com/v2/local/geo/transcoord.json";

    public KakaoDirectionsResponse getRoute(String origin, String destination, String waypoints) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_DIRECTIONS_URL)
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("priority", "RECOMMEND")
                .queryParam("summary", false);

        if (waypoints != null && !waypoints.isEmpty()) {
            builder.queryParam("waypoints", waypoints);
        }

        URI uri = builder.build().encode().toUri();
        log.info("카카오 길찾기 요청 URL: {}", uri);

        try {
            ResponseEntity<KakaoDirectionsResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    KakaoDirectionsResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("카카오 API 호출 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("길찾기 정보를 가져오는 데 실패했습니다.");
        }
    }

    public KakaoSearchResponse searchResponse(String keyword) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_SEARCH_URL)
                .queryParam("query", keyword)
                .queryParam("size", 10);

        URI uri = builder.build().encode().toUri();
        log.info("카카오 장소 검색 요청: {}", uri);

        try {
            ResponseEntity<KakaoSearchResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    KakaoSearchResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("장소 검색 실패: {}", e.getMessage());
            throw new RuntimeException("장소 검색 중 오류가 발생했습니다");
        }
    }

    public String getAddressFromCoords(String x, String y) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_COORD_URL)
                .queryParam("x", x)
                .queryParam("y", y);

        try {
            ResponseEntity<KakaoAddressResponse> response = restTemplate.exchange(
                    builder.build().toUri(),
                    HttpMethod.GET,
                    entity,
                    KakaoAddressResponse.class
            );

            KakaoAddressResponse body = response.getBody();
            if (body != null && !body.getDocuments().isEmpty()) {
                KakaoAddressResponse.Document doc = body.getDocuments().get(0);

                if (doc.getRoadAddress() != null) {
                    String fullAddress = doc.getRoadAddress().getAddressName();
                    String buildingName = doc.getRoadAddress().getBuildingName();

                    if (buildingName != null && !buildingName.isEmpty()) {
                        fullAddress += " (" + buildingName + ")";
                    }
                    return fullAddress;
                }
                else if (doc.getAddress() != null) {
                    return doc.getAddress().getAddressName();
                }
            }
            return "주소를 찾을 수 없음";

        } catch (Exception e) {
            log.error("주소 변환 실패", e.getMessage());
            throw new RuntimeException("주소 변환 중 오류가 발생했습니다");
        }
    }

    public KakaoTranscoordResponse getKTMCoords(String x, String y) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_TRANSCOORD_URL)
                .queryParam("x", x)
                .queryParam("y", y)
                .queryParam("input_coord", "WGS84")
                .queryParam("output_coord", "KTM")
                .build()
                .toUri();

        try {
            ResponseEntity<KakaoTranscoordResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    KakaoTranscoordResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("KTM 변환 실패: {}", e.getMessage());
            throw new RuntimeException("KTM 변환 호출 중 오류가 발생했습니다.");
        }
    }


    public KakaoTranscoordResponse getWGS84Coords(String x, String y) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_TRANSCOORD_URL)
                .queryParam("x", x)
                .queryParam("y", y)
                .queryParam("input_coord", "KTM")
                .queryParam("output_coord", "WGS84")
                .build()
                .toUri();

        try {
            ResponseEntity<KakaoTranscoordResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    KakaoTranscoordResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("WGS84 변환 실패: {}", e.getMessage());
            return null;
        }
    }

}