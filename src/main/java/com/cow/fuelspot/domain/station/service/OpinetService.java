package com.cow.fuelspot.domain.station.service;

import com.cow.fuelspot.domain.station.client.GasStationApiClient;
import com.cow.fuelspot.domain.station.dto.opinet.GasStationDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OpinetService {
    private GasStationApiClient gasStationApiClient;
    public List<GasStationDto> getNearbyGasStations(NearbyRequest request){
        GasStationDto[] dtos = gasStationApiClient.getNearbyGasStations(request);
        return Arrays.asList(dtos);
    }

    public OpinetDetailDto getDetailGasStation(String id){
        OpinetDetailDto dto = gasStationApiClient.getDetailGasStation(id);
        return dto;
    }

}
