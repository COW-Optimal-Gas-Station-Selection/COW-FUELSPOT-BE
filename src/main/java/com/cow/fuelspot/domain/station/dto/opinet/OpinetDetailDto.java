package com.cow.fuelspot.domain.station.dto.opinet;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OpinetDetailDto {
    private String UNI_ID;
    private String POLL_DIV_CO;
    private String OS_NM;
    private String VAN_ADR;
    private String NEW_ADR;
    private String TEL;
    private String SIGUNCD;
    private String LPG_YN;
    private String MAINT_YN;
    private String CAR_WASH_YN;
    private String KPETRO_YN;
    private String CVS_YN;
    private String GIS_X_COOR;
    private String GIS_Y_COOR;
    private String M_POLL_DIV_CO;
    private List<OilPriceDto> OIL_PRICE;

}
