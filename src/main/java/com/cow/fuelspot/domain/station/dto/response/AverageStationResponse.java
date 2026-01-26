package com.cow.fuelspot.domain.station.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AverageStationResponse {
    private Integer priceGasoline;
    private Integer priceDiesel;
    private Integer priceLpg;
    private Integer pricePremiumGasoline;
    private Integer priceKerosene;


}
