package com.cow.fuelspot.domain.station.repository;
import com.cow.fuelspot.domain.station.entity.GasStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GasStationRepository extends JpaRepository<GasStation, String>, GasStationRepositoryCustom {
    // 기본 CRUD 기능과 QueryDSL 기능을 모두 사용 가능
}