package com.cow.fuelspot.external.client;

import com.cow.fuelspot.external.dto.OpiNetAroundResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class OpiNetApiClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${opinet.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://www.opinet.co.kr/api/aroundAll.do";

    public OpiNetAroundResponseDTO fetchAroundStations(double x, double y, int radius, String prodCd) {
        URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("code", apiKey)
                .queryParam("out", "json")
                .queryParam("x", x)
                .queryParam("y", y)
                .queryParam("radius", radius)
                .queryParam("prodcd", prodCd)
                .queryParam("sort", 1)
                .build(true)
                .toUri();

        String res = restTemplate.getForObject(uri, String.class);

        try {
            return objectMapper.readValue(res, OpiNetAroundResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("OpiNet 응답이 JSON이 아님.");
        }
    }
}