package com.cow.fuelspot.domain.station.dto.opinet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
public class OpinetAverageDto {

        @JsonProperty("TRADE_DT")
        @JsonAlias("TradeDT")
        private String tradeDt;
        @JsonProperty("PRODCD")
        @JsonAlias("produce")
        private String prodCd;
        @JsonProperty("PRODNM")
        @JsonAlias("tradeDT")
        private String prodNm;
        @JsonProperty("PRICE")
        @JsonAlias("price")
        private String price;
        @JsonProperty("DIFF")
        @JsonAlias("diff")
        private String diff;

}