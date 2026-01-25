package com.cow.fuelspot.domain.station.dto.opinet;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
public class OpinetAverageDto{

    @JsonProperty("RESULT")
    private Result RESULT;

    public Result getRESULT() {
        return RESULT;
    }

    public void setRESULT(Result RESULT) {
        this.RESULT = RESULT;
    }

    public static class Result {

        @JsonProperty("OIL")
        private List<Oil> OIL;

        public List<Oil> getOIL() {
            return OIL;
        }

        public void setOIL(List<Oil> OIL) {
            this.OIL = OIL;
        }
    }

    public static class Oil {

        @JsonProperty("TRADE_DT")
        private String TRADE_DT;

        @JsonProperty("PRODCD")
        private String PRODCD;

        @JsonProperty("PRODNM")
        private String PRODNM;

        @JsonProperty("PRICE")
        private String PRICE;

        @JsonProperty("DIFF")
        private String DIFF;

        public String getTRADE_DT() {
            return TRADE_DT;
        }

        public void setTRADE_DT(String TRADE_DT) {
            this.TRADE_DT = TRADE_DT;
        }

        public String getPRODCD() {
            return PRODCD;
        }

        public void setPRODCD(String PRODCD) {
            this.PRODCD = PRODCD;
        }

        public String getPRODNM() {
            return PRODNM;
        }

        public void setPRODNM(String PRODNM) {
            this.PRODNM = PRODNM;
        }

        public String getPRICE() {
            return PRICE;
        }

        public void setPRICE(String PRICE) {
            this.PRICE = PRICE;
        }

        public String getDIFF() {
            return DIFF;
        }

        public void setDIFF(String DIFF) {
            this.DIFF = DIFF;
        }
    }
}

