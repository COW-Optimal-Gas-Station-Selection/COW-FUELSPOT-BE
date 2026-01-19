package com.cow.fuelspot.domain.station.repository;

import com.cow.fuelspot.domain.station.dto.GasStationDto;
import com.cow.fuelspot.domain.station.dto.GasStationRequest;

import java.util.List;

public interface GasStationRepositoryCustom {
    List<GasStationDto> findNearbyStations(GasStationRequest request);
}
