package com.cow.fuelspot.domain.station.service;

import com.cow.fuelspot.domain.station.client.GasStationApiClient;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpinetService {

    private final GasStationApiClient gasStationApiClient;

    public List<OpinetNearbyDto> getNearbyGasStations(NearbyRequest request){
        OpinetNearbyDto[] dtos = gasStationApiClient.getNearbyGasStations(request);
        return Arrays.asList(dtos);
    }

    public OpinetDetailDto getDetailGasStation(String id){
        return gasStationApiClient.getDetailGasStation(id);
    }
}