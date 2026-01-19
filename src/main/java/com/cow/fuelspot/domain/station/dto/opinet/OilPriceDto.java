package com.cow.fuelspot.domain.station.dto.opinet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OilPriceDto {
    @JsonProperty("PRODCD")
    private String type;      // B027, D047 등 코드

    @JsonProperty("PRICE")
    private Integer price;    // 가격

    @JsonProperty("TRADE_DT")
    private String tradeDate; // 거래일자

    @JsonProperty("TRADE_TM")
    private String tradeTime; // 거래시간
}
