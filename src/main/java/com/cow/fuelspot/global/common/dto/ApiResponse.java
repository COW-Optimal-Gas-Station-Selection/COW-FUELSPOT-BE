package com.cow.fuelspot.global.common.dto;

import com.cow.fuelspot.global.common.code.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 공통 응답 포맷
// 성공하든 실패하든 항상 {isSuccess, message, result} 구조 응닫
@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "message", "result"}) // 필드 순서 지정
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final boolean success;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    // 성공 응답 - 데이터 o
    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, "요청에 성공하였습니다.", result);
    }

    // 성공 응답 - 데이터 x
    public static <T> ApiResponse<T> onSuccess() {
        return new ApiResponse<>(true, "요청에 성공하였습니다.", null);
    }

    // 실패 응답 - ErrorCde
    public static <T> ApiResponse<T> onFailure(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getMessage(), null);
    }

    // 실패 응답 - 메시지 직접 입력
    public static <T> ApiResponse<T> onFailure(String message) {
        return new ApiResponse<>(false, message, null);
    }

    // 실패 응답 - 상세 데이터 포함
    public static <T> ApiResponse<T> onFailure(String message, T result) {
        return new ApiResponse<>(false, message, result);
    }
}
