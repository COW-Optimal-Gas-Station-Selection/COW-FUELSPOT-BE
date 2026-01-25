package com.cow.fuelspot.domain.station.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterResponse {
    private String stationId;
    private String name;
    private String brand;
    private String price;
    private Integer distance;
    private String lat;
    private String lon;
}
