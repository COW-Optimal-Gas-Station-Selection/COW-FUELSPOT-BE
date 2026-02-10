package com.cow.fuelspot.domain.car.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class BrandDto {
    private List<String> brands; // ["BMW", "기아", "현대"...]
}