package com.cow.fuelspot.domain.station.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum SortType {
    DISTANCE(1, "거리순"),
    PRICE(2, "가격순"),
    RECOMMEND(3, "추천순");

    private final Integer code;
    private final String description;

    // 숫자로 된 코드가 들어왔을 때 Enum으로 변환해주는 메서드
    public static SortType fromCode(Integer code) {
        return Arrays.stream(SortType.values())
                .filter(v -> Objects.equals(v.code, code))
                .findFirst()
                .orElse(DISTANCE); // 기본값: 거리순
    }
}