package com.cow.fuelspot.global.common.exception;

import com.cow.fuelspot.global.common.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 커스텀 예외
@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
