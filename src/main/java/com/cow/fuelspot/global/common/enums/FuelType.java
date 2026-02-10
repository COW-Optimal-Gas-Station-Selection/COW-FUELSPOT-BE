package com.cow.fuelspot.global.common.enums;

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
    LPG("K015", "자동차부탄"),
    PREMIUM_GASOLINE("B034", "고급휘발유"),
    KEROSENE("C004", "실내등유");

    private final String code;
    private final String description;

    @JsonValue
    public String toValue() {
        return this.name();
    }

    @JsonCreator
    public static FuelType fromCode(String code) {
        if (code == null) return null;

        try {
            return FuelType.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Arrays.stream(FuelType.values())
                    .filter(type -> type.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }

    public static FuelType fromCsvName(String csvName) {
        try {
            return FuelType.valueOf(csvName.trim().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

}
