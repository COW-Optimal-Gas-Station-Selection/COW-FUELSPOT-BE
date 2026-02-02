package com.cow.fuelspot.domain.station.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum Sido {
    SEOUL("서울", "01"),
    GYEONGGI("경기", "02"),
    GANGWON("강원", "03"),
    CHUNGBUK("충북", "04"),
    CHUNGNAM("충남", "05"),
    JEONBUK("전북", "06"),
    JEONNAM("전남", "07"),
    GYEONGBUK("경북", "08"),
    GYEONGNAM("경남", "09"),
    BUSAN("부산", "10"),
    JEJU("제주", "11");

    private final String name;
    private final String code;

    Sido(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() { return name; }

    @JsonValue
    public String getCode() {
        return code;
    }

    public static Sido fromCode(String code) {
        return Arrays.stream(Sido.values())
                .filter(type -> type.code.equals(code))
                .findFirst()
                .orElse(null);
    }
}
