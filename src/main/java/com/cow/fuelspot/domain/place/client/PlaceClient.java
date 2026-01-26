package com.cow.fuelspot.domain.place.client;

import com.cow.fuelspot.domain.place.dto.PlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class PlaceClient {
    private final RestTemplate restTemplate;
    @Value("${KAKAO_API_KEY}")
    private String apiKey;
    private static final String BASE_URL = "https://dapi.kakao.com";
    //키워드로 장소 검색
    public PlaceDto searchPlaces(String keyword) {
        try {
            System.out.println(keyword);
            String url = String.valueOf(UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .path("/v2/local/search/keyword.json")
                    .queryParam("query", keyword)
                    .build()
                    .toUri());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + apiKey);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<PlaceDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    PlaceDto.class
            );

            return response.getBody();

        } catch (Exception e) {
            return PlaceDto.builder()
                    .places(Collections.emptyList())
                    .build();
        }
    }



}

