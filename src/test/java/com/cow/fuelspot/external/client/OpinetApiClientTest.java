package com.cow.fuelspot.external.client;

import com.cow.fuelspot.external.dto.OpinetAroundResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OpinetApiClientTest {

    @Autowired
    private OpinetApiClient opinetApiClient;

    @Test
    @DisplayName("오피넷 API 연동 테스트 - 서울역 주변 휘발유 주유소 조회")
    void fetchAroundStationsTest() {
        // given: 테스트 데이터 준비 (서울역 좌표)
        double x = 309907.0;
        double y = 550882.0;
        int radius = 3000;
        String prodCd = "B027";

        OpinetAroundResponseDTO response = opinetApiClient.fetchAroundStations(x, y, radius, prodCd);

        System.out.println("--------- 오피넷 응답 확인 ---------");

        assertThat(response).isNotNull();
        assertThat(response.getRESULT()).isNotEmpty();

        List<OpinetAroundResponseDTO.GasStationDTO> stations = response.getRESULT();
        System.out.println("총 발견된 주유소 개수: " + stations.size() + "개");

        for (int i = 0; i <= stations.size() - 1; i++) {
            OpinetAroundResponseDTO.GasStationDTO station = stations.get(i);
            System.out.println("--------------------------------");
            System.out.println("주유소명 : " + station.getOsNm());
            System.out.println("가격    : " + station.getPrice() + "원");
            System.out.println("상표    : " + station.getPollDivCd());
            System.out.println("거리    : " + station.getDistance() + "m");
        }
        System.out.println("--------------------------------");
    }
}