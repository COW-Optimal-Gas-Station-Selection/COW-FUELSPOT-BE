package com.cow.fuelspot.domain.station.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FuelType {
    GASOLINE("B027", "휘발유"),
    DIESEL("D047", "경유"),
    LPG("K015", "자동차부탄");

    private final String code;
    private final String description;

    @JsonValue
    public String toString() {
        return this.code; // "B027" 반환
    }
}