package com.cow.fuelspot.external.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OpinetAroundResponseDTO {

    private List<GasStationDTO> RESULT;

    @Getter
    @Setter
    public static class GasStationDTO {
        private String UNI_ID;
        private String POLL_DIV_CD;
        private String OS_NM;
        private int PRICE;
        private double DISTANCE;
        private double GIS_X_COOR;
        private double GIS_Y_COOR;
    }
}