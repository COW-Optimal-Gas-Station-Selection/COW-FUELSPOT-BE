package com.cow.fuelspot.domain.car.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicCarDto {
    @JsonProperty("COMP_NM")
    private String compNm;    // 제조사 (현대)

    @JsonProperty("MODEL_NM")
    private String modelName; // 모델명 (아반떼)

    @JsonProperty("YEAR")
    private String year;      // 연식 (2024)

    @JsonProperty("FUEL_NM")
    private String fuelName;  // 유종 (휘발유)

    @JsonProperty("DISPLAY_EFF")
    private Double efficiency; // 연비 (15.3)
}