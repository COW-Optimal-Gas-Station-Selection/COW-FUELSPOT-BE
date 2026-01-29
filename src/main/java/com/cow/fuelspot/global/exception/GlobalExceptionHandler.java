package com.cow.fuelspot.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

// 전역 예외 처리기
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 잘못된 요청 데이터 (이메일 중복, 재발급 토큰 불일치)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handlerCommonException(IllegalArgumentException e) {
        return createErrorResponse(e.getMessage());
    }

    // 로그인 실패 에러 (아이디/비밀번호)
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleLoginException(Exception e) {
        return createErrorResponse("아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    // @Valid 유효성 검사 실패 (빈칸, 이메일 형식 틀림 등)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        // 에러 메시지 중 첫 번째 (ex. 이메일을 입력해주세요)
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return createErrorResponse(errorMessage);
    }

    // JSON 파싱 에러 (Enum 타입 불일치, 날짜 형식 오류 등)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonException(HttpMessageNotReadableException e) {
        return createErrorResponse("올바르지 않은 데이터 형식입니다. (유종, 날짜 등을 확인해주세요)");
    }

    // 그 외 알 수 없는 모든 에러 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllException(Exception e) {
        // 서버 로그에 에러 내용 (디버깅용)
        e.printStackTrace();
        return createErrorResponse("알 수 없는 오류가 발생했습니다.");
    }

    // 에러 응답 생성
    private ResponseEntity<Map<String, Object>> createErrorResponse(String message) {

        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", false);
        response.put("message", message);

        return ResponseEntity.ok(response);
    }
}
