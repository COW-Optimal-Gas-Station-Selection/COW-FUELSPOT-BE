package com.cow.fuelspot.domain.station.dto.opinet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpinetDetailDto {

    @JsonProperty("id")
    @JsonAlias("UNI_ID")
    private String id;//주유소 코드

    @JsonProperty("brand")
    @JsonAlias({"POLL_DIV_CD"})
    private String brand;//상표

    @JsonProperty("name")
    @JsonAlias("OS_NM")
    private String name;//상호

    @JsonProperty("addressOld")
    @JsonAlias("VAN_ADR")
    private String addressOld;//지번 주소

    @JsonProperty("addressNew")
    @JsonAlias("NEW_ADR")
    private String addressNew;//도로명 주소

    @JsonProperty("tel")
    @JsonAlias("TEL")
    private String tel;//전화번호

    @JsonProperty("sigunCode")
    @JsonAlias("SIGUNCD")
    private String sigunCode;//소재지역 시군 코드

    @JsonProperty("lpgYn")
    @JsonAlias("LPG_YN")
    private String lpgYn;//업종 구분

    @JsonProperty("maintenanceYn")
    @JsonAlias("MAINT_YN")
    private String maintenanceYn;//경정비 시설 존재 여부

    @JsonProperty("carWashYn")
    @JsonAlias("CAR_WASH_YN")
    private String carWashYn;//세차장 존재 여부

    @JsonProperty("kpetroYn")
    @JsonAlias("KPETRO_YN")
    private String kpetroYn;//품질 인증주유소 여부

    @JsonProperty("cvsYn") //세차장 여부
    @JsonAlias("CVS_YN")
    private String cvsYn;

    @JsonProperty("lat")
    @JsonAlias("GIS_X_COOR")
    private String lat;

    @JsonProperty("lon")
    @JsonAlias("GIS_Y_COOR")
    private String lon;

    @JsonProperty("oilPrices")
    @JsonAlias("OIL_PRICE")
    private List<OilPriceDto> oilPrices;

}