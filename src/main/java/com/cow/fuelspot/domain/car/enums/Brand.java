package com.cow.fuelspot.domain.car.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Brand {

    HYUNDAI("현대", "현대"),
    KIA("기아", "기아"),
    GENESIS("제네시스", "현대"),
    BMW("BMW", "BMW"),
    BENZ("벤츠", "벤츠"),
    AUDI("아우디", "아우디");

    private final String name;
    private final String apiKeyword;
}