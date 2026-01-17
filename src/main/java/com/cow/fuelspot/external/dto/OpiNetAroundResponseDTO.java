package com.cow.fuelspot.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OpiNetAroundResponseDTO {

    @JsonProperty("RESULT")
    private Result result;

    @Getter
    @Setter
    public static class Result {
        @JsonProperty("OIL")
        private List<GasStationDTO> statonList;
    }

    @Getter
    @Setter
    public static class GasStationDTO {

        @JsonProperty("UNI_ID")
        private String uniId;

        @JsonProperty("POLL_DIV_CD")
        private String pollDivCd;

        @JsonProperty("OS_NM")
        private String osNm;

        @JsonProperty("PRICE")
        private int price;

        @JsonProperty("DISTANCE")
        private double distance;

        @JsonProperty("GIS_X_COOR")
        private double gisXCoor;

        @JsonProperty("GIS_Y_COOR")
        private double gisYCoor;

    }
}