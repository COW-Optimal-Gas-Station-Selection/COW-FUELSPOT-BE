package com.cow.fuelspot.domain.auth.repository;

import com.cow.fuelspot.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    // save(), findById(), deleteById() 사용 가능
}