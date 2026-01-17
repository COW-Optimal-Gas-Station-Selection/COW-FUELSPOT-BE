package com.cow.fuelspot.fuelQuery.repository;

import com.cow.fuelspot.fuelQuery.dto.GasStationDto;
import com.cow.fuelspot.fuelQuery.dto.GasStationRequest;

import java.util.List;

public interface GasStationRepositoryCustom {
    List<GasStationDto> findNearbyStations(GasStationRequest request);
}
