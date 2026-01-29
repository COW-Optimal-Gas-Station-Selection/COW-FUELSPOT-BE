package com.cow.fuelspot.domain.auth.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// 리프레시 토큰 엔티티
@Getter
@NoArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    private String email;

    // Refresh Token 값
    private String value;

    @Builder
    public RefreshToken(String email, String value) {
        this.email = email;
        this.value = value;
    }

    // 토큰 교체 (Refresh Token Rotation)
    public void updateValue(String token) {
        this.value = token;
    }
}