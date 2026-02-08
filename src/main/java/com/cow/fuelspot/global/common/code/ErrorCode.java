package com.cow.fuelspot.global.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 에러 코드 정의서
// 에러 메세지, HTTP 상태 코드 통합 관리
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 공통 에러
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY, "외부 API 호출 중 오류가 발생했습니다."),

    // 인증/인가
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    LOGOUT_FAILED(HttpStatus.BAD_REQUEST, "로그아웃 실패: 로그인 정보가 없습니다."),

    // 회원
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다."),
    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "새 비밀번호는 기존 비밀번호와 다르게 설정해야 합니다."),
    PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // 이메일
    UNABLE_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 발송에 실패했습니다."),
    VERIFICATION_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "유효하지 않거나 만료된 인증 코드입니다."),
    VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),

    // 주유소 서비스
    STATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 아이디로 조회된 주유소 상세 정보가 없습니다."),
    STATION_API_COMMUNICATION_ERROR(HttpStatus.BAD_GATEWAY, "주유소 서버 통신에 실패했습니다."),
    STATION_DATA_PARSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 분석 중 오류가 발생했습니다."),
    STATION_SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "정보 처리 과정에서 시스템 오류가 발생했습니다."),
    STATION_NO_CONTENT(HttpStatus.NO_CONTENT, "조회된 정보가 없습니다."),
    DUPLICATE_FAVORITE(HttpStatus.CONFLICT, "이미 즐겨찾기에 등록된 주유소입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
