package com.cow.fuelspot.domain.station.client.controller;

import com.cow.fuelspot.domain.station.controller.GasStationController;
import com.cow.fuelspot.domain.station.dto.opinet.GasStationDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.cow.fuelspot.domain.station.service.OpinetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GasStationController.class)
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpinetService opinetService;

    @Test
    @DisplayName("주변 주유소 조회 API 테스트")
    void getNearbyStations() throws Exception {
        // given - Java 필드명 사용
        GasStationDto station1 = GasStationDto.builder()
                .id("A0011826")
                .name("만남의광장주유소")
                .brand("현대오일뱅크")
                .price(1654)
                .distance(500)
                .build();

        GasStationDto station2 = GasStationDto.builder()
                .id("A0022345")
                .name("서울주유소")
                .brand("SK에너지")
                .price(1680)
                .distance(800)
                .build();

        List<GasStationDto> mockStations = Arrays.asList(station1, station2);

        when(opinetService.getNearbyGasStations(any(NearbyRequest.class)))
                .thenReturn(mockStations);

        // when & then - JSON 필드명으로 검증
        mockMvc.perform(get("/api/gas-stations/nearby")
                        .param("lat", "37.4814")
                        .param("lon", "127.0377")
                        .param("radius", "3000")
                        .param("fuelType", "GASOLINE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].UNI_ID").value("A0011826"))
                .andExpect(jsonPath("$[0].OS_NM").value("만남의광장주유소"))
                .andExpect(jsonPath("$[0].PRICE").value(1654))
                .andExpect(jsonPath("$[1].UNI_ID").value("A0022345"));
    }

    @Test
    @DisplayName("주유소 상세 정보 조회 API 테스트")
    void getStationDetail() throws Exception {
        // given
        String stationId = "A0011826";

        OpinetDetailDto detailDto = OpinetDetailDto.builder()
                .UNI_ID(stationId)
                .OS_NM("만남의광장주유소")
                .POLL_DIV_CO("현대오일뱅크")
                .TEL("02-573-7430")
                .NEW_ADR("서울 서초구 양재대로12길 73-71")
                .CAR_WASH_YN("N")
                .CVS_YN("N")
                .MAINT_YN("N")
                .build();

        when(opinetService.getDetailGasStation(eq(stationId)))
                .thenReturn(detailDto);

        // when & then - DetailResponse로 변환되므로 변환된 필드명
        mockMvc.perform(get("/api/gas-stations/{stationId}", stationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(stationId))
                .andExpect(jsonPath("$.name").value("만남의광장주유소"))
                .andExpect(jsonPath("$.brand").value("현대오일뱅크"))
                .andExpect(jsonPath("$.tel").value("02-573-7430"))
                .andExpect(jsonPath("$.address").value("서울 서초구 양재대로12길 73-71"))
                .andExpect(jsonPath("$.carWash").value(false));
    }

    @Test
    @DisplayName("잘못된 파라미터로 주변 주유소 조회 시 400 에러")
    void getNearbyStations_withInvalidParams() throws Exception {
        // when & then - lat 파라미터 누락
        mockMvc.perform(get("/api/gas-stations/nearby")
                        .param("lon", "127.0377")
                        .param("radius", "3000")
                        .param("fuelType", "GASOLINE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주변에 주유소가 없을 때 빈 배열 반환")
    void getNearbyStations_emptyResult() throws Exception {
        // given
        when(opinetService.getNearbyGasStations(any(NearbyRequest.class)))
                .thenReturn(Arrays.asList());

        // when & then
        mockMvc.perform(get("/api/gas-stations/nearby")
                        .param("lat", "37.4814")
                        .param("lon", "127.0377")
                        .param("radius", "3000")
                        .param("fuelType", "GASOLINE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}