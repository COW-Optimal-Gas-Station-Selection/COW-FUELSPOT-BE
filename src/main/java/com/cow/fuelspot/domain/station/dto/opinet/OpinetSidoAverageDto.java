package com.cow.fuelspot.domain.station.dto.opinet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OpinetSidoAverageDto {
    @JsonProperty("SIDOCD")
    @JsonAlias("sidoCd")
    private String sidoCd;

    @JsonProperty("SIDONM")
    @JsonAlias("sidoNm")
    private String sidoNm;

    @JsonProperty("PRODCD")
    @JsonAlias("prodCd")
    private String prodCd;

    @JsonProperty("PRICE")
    @JsonAlias("price")
    private Double price;

    @JsonProperty("DIFF")
    @JsonAlias("diff")
    private Double diff;
}
