package com.cow.fuelspot.global.common.exception;

import com.cow.fuelspot.global.common.code.ErrorCode;
import com.cow.fuelspot.global.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException e) {
        log.warn("Business Exception: {}", e.getErrorCode().getMessage()); // 로그 기록

        // HTTP 상태 코드와 데이터 모두 전송
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.onFailure(e.getErrorCode()));
    }

    // 유효성 검사 실패 처리 (@Valid 어노테이션 위반 시)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        // 에러가 여러 개일 수 있으니 Map에 담음
        Map<String, String> errors = new HashMap<>();

        // 발생한 모든 에러 Map에 삽입
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField(); // 틀린 필드명
            String errorMessage = error.getDefaultMessage(); // 에러 메세지
            errors.put(fieldName, errorMessage);
        });

        log.warn("Validation Exception: {}", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.onFailure("입력값이 올바르지 않습니다.", errors));
    }

    // 로그인 실패 처리 (비밀번호 불일치, 해당 아이디 존재 x)
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse<Object>> handleLoginException(Exception e) {
        log.warn("Login Failed: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.onFailure("아이디 또는 비밀번호가 일치하지 않습니다."));
    }

    // JSON 파싱 실패 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleJsonException(HttpMessageNotReadableException e) {
        log.warn("JSON parse Error: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.onFailure("올바르지 않은 데이터 형식입니다."));
    }

    // 잘못된 인자값 전달 시
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Argument Error: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.onFailure(e.getMessage()));
    }

    // 필수 파라미터가 누락되었을 때 발생하는 에러
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParamsException(MissingServletRequestParameterException e) {
        String message = "필수 파라미터 '" + e.getParameterName() + "'이(가) 누락되었습니다.";
        log.warn("Missing Parameter: {}", message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.onFailure(message));
    }

    // 그 외 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        log.error("Unhandled Exception: ", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.onFailure(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
