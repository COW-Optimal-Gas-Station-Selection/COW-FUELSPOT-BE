package com.cow.fuelspot.domain.auth.repository;

import com.cow.fuelspot.domain.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    // save(), findById(), deleteById() 사용 가능
}