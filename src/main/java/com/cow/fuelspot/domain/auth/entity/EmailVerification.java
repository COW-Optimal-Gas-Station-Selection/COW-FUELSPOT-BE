package com.cow.fuelspot.domain.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// Redis에 저장되는 객체
// value: Redis 키의 앞부분으로 사용
// timeToLive: 데이터의 유효 시간 (5분), 시간이 지나면 자동 삭제
@RedisHash(value = "email_verification", timeToLive = 300)
public class EmailVerification {

    // Redis에서 데이터를 식별하는 고유 키 (이메일)
    @Id
    private String email;

    // 인증 코드 값
    private String code;
}
