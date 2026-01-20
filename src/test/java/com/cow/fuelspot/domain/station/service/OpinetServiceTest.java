package com.cow.fuelspot.domain.station.service;

import com.cow.fuelspot.domain.station.client.GasStationApiClient;
import com.cow.fuelspot.domain.station.dto.enums.FuelType;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpinetServiceTest {

    @Mock
    private GasStationApiClient gasStationApiClient;

    @InjectMocks
    private OpinetService opinetService;

    @Test
    @DisplayName("유종별로 흩어진 주유소 정보를 하나의 응답으로 병합한다")
    void mergeNearbyGasStations() {
        NearbyRequest request = NearbyRequest.builder()
                .lat(37.4814)
                .lon(127.0377)
                .radius(3000)
                .build();

        OpinetNearbyDto gasolineDto = OpinetNearbyDto.builder()
                .id("A001")
                .name("테스트주유소")
                .brand("SK")
                .price(1600)
                .distance(500)
                .build();

        OpinetNearbyDto dieselDto = OpinetNearbyDto.builder()
                .id("A001")
                .name("테스트주유소")
                .brand("SK")
                .price(1500)
                .distance(500)
                .build();

        OpinetNearbyDto lpgDto = OpinetNearbyDto.builder()
                .id("A001")
                .name("테스트주유소")
                .brand("SK")
                .price(1000)
                .distance(500)
                .build();

        when(gasStationApiClient.getNearbyGasStations(any(), eq(FuelType.GASOLINE)))
                .thenReturn(new OpinetNearbyDto[]{gasolineDto});
        when(gasStationApiClient.getNearbyGasStations(any(), eq(FuelType.DIESEL)))
                .thenReturn(new OpinetNearbyDto[]{dieselDto});
        when(gasStationApiClient.getNearbyGasStations(any(), eq(FuelType.LPG)))
                .thenReturn(new OpinetNearbyDto[]{lpgDto});

        List<NearbyResponse> results = opinetService.getNearbyGasStations(request);

        assertThat(results).hasSize(1);
        NearbyResponse response = results.get(0);
        assertThat(response.getId()).isEqualTo("A001");
        assertThat(response.getPriceGasoline()).isEqualTo(1600);
        assertThat(response.getPriceDiesel()).isEqualTo(1500);
        assertThat(response.getPriceLpg()).isEqualTo(1000);

        System.out.println("\n[Service Merge Result]");
        results.forEach(res -> System.out.println("ID: " + res.getId() +
                ", 가솔린: " + res.getPriceGasoline() +
                ", 디젤: " + res.getPriceDiesel() +
                ", LPG: " + res.getPriceLpg()));
    }
}