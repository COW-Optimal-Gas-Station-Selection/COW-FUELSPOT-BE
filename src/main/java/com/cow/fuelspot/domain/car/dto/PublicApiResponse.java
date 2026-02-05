package com.cow.fuelspot.domain.car.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicApiResponse {

    @JsonProperty("data") // 공공데이터 API 스펙에 따라 'data', 'body', 'items' 중 확인 필요
    private List<PublicCarDto> data;

    @JsonProperty("page")
    private int page;

    @JsonProperty("perPage")
    private int perPage;

    @JsonProperty("totalCount")
    private int totalCount;
}