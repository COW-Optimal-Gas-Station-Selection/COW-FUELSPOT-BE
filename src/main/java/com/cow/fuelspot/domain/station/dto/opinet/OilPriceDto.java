package com.cow.fuelspot.domain.station.dto.opinet;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OilPriceDto {

    @JsonProperty("fuelType")
    @JsonAlias("PRODCD")
    private FuelType type;

    @JsonProperty("price")
    @JsonAlias("PRICE")
    private Integer price;

    @JsonProperty("tradeDate")
    @JsonAlias("TRADE_DT")
    private String tradeDate; // 거래일자

    @JsonProperty("tradeTime")
    @JsonAlias("TRADE_TM")
    private String tradeTime; // 거래시간
}