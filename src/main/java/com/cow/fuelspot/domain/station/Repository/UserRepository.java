package com.cow.fuelspot.domain.station.Repository;

import com.cow.fuelspot.global.common.enums.FuelType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public FuelType findFuelTypeByMemberId(String memberId) {
        String sql = "SELECT fuel_type FROM member WHERE email = ?";
        try {
            String fuelTypeStr = jdbcTemplate.queryForObject(sql, String.class, memberId);
            return fuelTypeStr != null ? FuelType.valueOf(fuelTypeStr) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public Double findCarFuelEfficiency(String memberId) {
        String sql = "SELECT car_fuel_efficiency FROM member WHERE email = ?";
        try {
            Double fuelEfficiency = jdbcTemplate.queryForObject(sql, Double.class, memberId);
            return fuelEfficiency != null ? fuelEfficiency : 0.0;
        } catch (Exception e) {
            return null;
        }
    }

}
