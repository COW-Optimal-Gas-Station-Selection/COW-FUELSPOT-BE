package com.cow.fuelspot.domain.station.Repository;

import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public FuelType findFuelTypeByMemberId(String memberId) {
        String sql = "SELECT fuelType FROM member WHERE id = ?";
        try {
            String fuelTypeStr = jdbcTemplate.queryForObject(sql, String.class, memberId);
            return fuelTypeStr != null ? FuelType.valueOf(fuelTypeStr) : null;
        } catch (Exception e) {
            return null;
        }
    }
}