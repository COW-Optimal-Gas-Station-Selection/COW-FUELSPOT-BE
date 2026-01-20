package com.cow.fuelspot.domain.station.client.controller;

import com.cow.fuelspot.domain.station.controller.GasStationController;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;
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

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GasStationController.class)
@AutoConfigureMockMvc(addFilters = false)
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpinetService opinetService;

    @Test
    @DisplayName("주변 주유소 목록 JSON 반환값 출력 테스트")
    void printNearbyJsonResponse() throws Exception {
        NearbyResponse response = NearbyResponse.builder()
                .id("A0011826")
                .name("만남의광장주유소")
                .brand("현대오일뱅크")
                .priceGasoline(1654)
                .distance(500)
                .lat("37.4814")
                .lon("127.0377")
                .build();

        when(opinetService.getNearbyGasStations(any(NearbyRequest.class)))
                .thenReturn(Collections.singletonList(response));

        MvcResult result = mockMvc.perform(get("/api/gas-stations/nearby")
                        .param("lat", "37.4814")
                        .param("lon", "127.0377")
                        .param("radius", "3000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("\n[Nearby Gas Stations JSON Response]");
        System.out.println(content);
        System.out.println("-----------------------------------\n");
    }

//    @Test
//    @DisplayName("상세 정보 JSON 반환값 출력 테스트")
//    void printDetailJsonResponse() throws Exception {
//        String stationId = "A0011826";
//        OpinetDetailDto detailDto = OpinetDetailDto.builder()
//                .id(stationId)
//                .name("만남의광장주유소")
//                .build();
//
//        when(opinetService.getDetailGasStation(stationId)).thenReturn(detailDto);
//
//        MvcResult result = mockMvc.perform(get("/api/gas-stations/{stationId}", stationId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        System.out.println("\n[Gas Station Detail JSON Response]");
//        System.out.println(content);
//        System.out.println("----------------------------------\n");
//    }
}