package com.cow.fuelspot.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// 전역 예외 처리기
@RestControllerAdvice
public class GlobalExceptionHandler {

    // IllegalArgumentException 에러 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handlerCommonException(IllegalArgumentException e) {

        // 에러 대신 JSON 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", false);
        response.put("message", e.getMessage());

        return ResponseEntity.ok(response);
    }
}
