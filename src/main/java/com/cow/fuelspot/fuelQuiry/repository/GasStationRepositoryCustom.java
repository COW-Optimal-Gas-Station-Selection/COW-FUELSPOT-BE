package com.cow.fuelspot.fuelQuiry.repository;

import com.cow.fuelspot.fuelQuiry.dto.GasStationDto;
import com.cow.fuelspot.fuelQuiry.dto.GasStationRequest;

import java.util.List;

public interface GasStationRepositoryCustom {
    List<GasStationDto> findNearbyStations(GasStationRequest request);
}
