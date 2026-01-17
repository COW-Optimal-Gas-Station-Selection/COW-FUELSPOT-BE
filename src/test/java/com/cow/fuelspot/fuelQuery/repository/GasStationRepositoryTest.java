package com.cow.fuelspot.fuelQuery.repository;

import com.cow.fuelspot.config.QuerydslConfig;
import com.cow.fuelspot.fuelQuery.dto.FuelType;
import com.cow.fuelspot.fuelQuery.dto.GasStationDto;
import com.cow.fuelspot.fuelQuery.dto.GasStationRequest;
import com.cow.fuelspot.fuelQuery.entity.GasStation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import org.springframework.context.annotation.Import;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 핵심: 실제 DB 사용 설정
// @ActiveProfiles("test") // 만약 테스트용 application-test.yml이 따로 있다면 주석을 해제하세요.
class GasStationRepositoryTest {

    @Autowired
    private GasStationRepository gasStationRepository;

    @Test
    @DisplayName("반경 3km 이내의 주유소가 거리순으로 조회되는지 테스트")
    void findNearbyStationsDistanceSortTest() {
        // 1. Given: 샘플 데이터 준비 (테스트 후 자동 Rollback 됩니다)
        GasStation gs1 = GasStation.builder()
                .stationId("A001")
                .name("강남주유소")
                .brand("SK")
                .priceGasoline(1600)
                .priceDiesel(2000)
                .distance(1.2)
                .lat(37.1234).lon(127.1234)
                .build();

        GasStation gs2 = GasStation.builder()
                .stationId("A002")
                .name("역삼주유소")
                .brand("GS")
                .priceGasoline(1500)
                .priceDiesel(1900)
                .distance(2.5)
                .lat(37.5678).lon(127.5678)
                .build();

        gasStationRepository.saveAll(List.of(gs1, gs2));

        // 2. When: 거리순(sort=1)으로 요청
        GasStationRequest request = GasStationRequest.builder()
                .lat(37.0000).lon(127.0000)
                .radius(3000.0)
                .fuelType(FuelType.GASOLINE)
                .sort(1) // 거리순
                .build();

        List<GasStationDto> result = gasStationRepository.findNearbyStations(request);

        // 3. Then: 검증
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getName()).isEqualTo("강남주유소"); // 1.2km인 강남이 먼저 나와야 함

    }
}