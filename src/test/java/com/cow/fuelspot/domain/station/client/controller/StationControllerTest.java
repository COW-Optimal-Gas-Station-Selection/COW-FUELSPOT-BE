package com.cow.fuelspot.domain.station.client.controller;

import com.cow.fuelspot.domain.station.controller.GasStationController;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.cow.fuelspot.domain.station.service.OpinetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GasStationController.class)
@AutoConfigureMockMvc(addFilters = false)
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpinetService opinetService;

    @Test
    @DisplayName("주변 주유소 목록 필드 단위 출력 테스트")
    void printNearbyFields() throws Exception {
        OpinetNearbyDto station = OpinetNearbyDto.builder()
                .id("A0011826")
                .name("만남의광장주유소")
                .brand("현대오일뱅크")
                .price(1654)
                .distance(500)
                .build();

        when(opinetService.getNearbyGasStations(any(NearbyRequest.class)))
                .thenReturn(Arrays.asList(station));

        MvcResult result = mockMvc.perform(get("/api/gas-stations/nearby")
                        .param("lat", "37.4814")
                        .param("lon", "127.0377")
                        .param("radius", "3000")
                        .param("fuelType", "GASOLINE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].UNI_ID").exists())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        System.out.println("======= Nearby Gas Station 분석 =======");
        System.out.println("Raw JSON: " + content);
        System.out.println("======================================");
    }

    @Test
    @DisplayName("상세 정보 필드 단위 출력 테스트")
    void printDetailFields() throws Exception {
        String stationId = "A0011826";
        OpinetDetailDto detailDto = OpinetDetailDto.builder()
                .id(stationId)
                .name("만남의광장주유소")
                .build();

        when(opinetService.getDetailGasStation(stationId)).thenReturn(detailDto);

        MvcResult result = mockMvc.perform(get("/api/gas-stations/{stationId}", stationId))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        System.out.println("======= Detail Information 분석 =======");
        System.out.println("주유소 ID: " + stationId);
        System.out.println("응답 바디: " + content);
        System.out.println("======================================");
    }
}