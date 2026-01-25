package com.cow.fuelspot.domain.station.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum FuelType {
    GASOLINE("B027", "휘발유"),
    DIESEL("D047", "경유"),
    LPG("K015", "자동차부탄");

    private final String code;
    private final String description;

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static FuelType fromCode(String code) {
        if (code == null) return null;

        return Arrays.stream(FuelType.values())
                .filter(type -> type.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}