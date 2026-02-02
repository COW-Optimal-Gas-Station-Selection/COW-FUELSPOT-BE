package com.cow.fuelspot.domain.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

// 리프레시 토큰 엔티티
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "refresh_token", timeToLive = 604800)
public class RefreshToken {

    @Id
    private String email;

    // Refresh Token 값
    private String value;

    // 토큰 교체 (Refresh Token Rotation)
    public void updateValue(String token) {
        this.value = token;
    }
}