package com.cow.fuelspot.external.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JacksonXmlRootElement(localName = "RESULT")
public class OpinetAroundResponseDTO {

    @JacksonXmlProperty(localName = "OIL")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<GasStationDTO> RESULT;

    @Getter
    @Setter
    public static class GasStationDTO {
        @JacksonXmlProperty(localName = "UNI_ID")
        private String uniId;

        @JacksonXmlProperty(localName = "POLL_DIV_CO")
        private String pollDivCd;

        @JacksonXmlProperty(localName = "OS_NM")
        private String osNm;

        @JacksonXmlProperty(localName = "PRICE")
        private int price;

        @JacksonXmlProperty(localName = "DISTANCE")
        private double distance;

        @JacksonXmlProperty(localName = "GIS_X_COOR")
        private double gisXCoor;

        @JacksonXmlProperty(localName = "GIS_Y_COOR")
        private double gisYCoor;
    }
}