package com.cow.fuelspot.fuelQuiry.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FuelType {
    GASOLINE("B027", "휘발유"),
    DIESEL("D047", "경유");
    //PREMIUM_GASOLINE("B034", "고급휘발유"),
    //KEROSENE("C004", "실내등유"),
    //LPG("K015", "자동차부탄");

    private final String code;        // DB 및 API용 코드 (B027 등)
    private final String description; // 한글 명칭
}
