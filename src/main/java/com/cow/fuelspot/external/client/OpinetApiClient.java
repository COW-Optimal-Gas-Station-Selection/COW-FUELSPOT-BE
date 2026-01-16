package com.cow.fuelspot.external.client;

import com.cow.fuelspot.external.dto.OpinetAroundResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@Component
public class OpinetApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${opinet.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://www.opinet.co.kr/api/aroundAll.do";

    public OpinetAroundResponseDTO fetchAroundStations(double x, double y, int radius, String prodCd) {

        URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("code", apiKey)
                .queryParam("out", "xml")      // ★ 핵심: XML 요청
                .queryParam("x", x)
                .queryParam("y", y)
                .queryParam("radius", radius)
                .queryParam("prodcd", prodCd)
                .queryParam("sort", 1)
                .build()
                .toUri();

        return restTemplate.getForObject(uri, OpinetAroundResponseDTO.class);
    }
}