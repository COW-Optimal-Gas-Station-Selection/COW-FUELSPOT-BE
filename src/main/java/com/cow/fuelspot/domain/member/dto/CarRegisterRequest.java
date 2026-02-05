package com.cow.fuelspot.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CarRegisterRequest {
    private String carName;
    private Double efficiency;
}