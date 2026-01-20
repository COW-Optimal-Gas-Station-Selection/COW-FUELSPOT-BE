//package com.cow.fuelspot.domain.station.client;
//
//import com.cow.fuelspot.domain.station.dto.enums.FuelType;
//import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
//import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
//import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import tools.jackson.databind.ObjectMapper;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class OpinetAPIClinetTest {
//
//    private GasStationApiClient gasStationApiClient;
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        // 실제 API 호출을 위해 Mock 서버 설정 없이 클라이언트를 생성합니다.
//        objectMapper = new ObjectMapper();
//        gasStationApiClient = new GasStationApiClient(objectMapper);
//    }
//
//    @Test
//    @DisplayName("주변 주유소 목록 실제 호출 및 콘솔 출력")
//    void printNearbyGasStations() throws Exception {
//        // 1. Given: 요청 파라미터 (좌표는 KATECH 좌표계인지 확인 필요, 일단 예시값)
//        NearbyRequest request = NearbyRequest.builder()
//                .lat(Double.valueOf("314681.8")) // 오피넷은 보통 KATECH(TM) 좌표를 사용합니다.
//                .lon(Double.valueOf("544837.0"))
//                .radius(5000)
//                .build();
//
//        // 2. When: 실제 API 호출
//        System.out.println(">>> 호출 URL 확인을 위한 로그 출력 필요 (GasStationApiClient 내부)");
//        OpinetNearbyDto[] result = gasStationApiClient.getNearbyGasStations(request, FuelType.GASOLINE);
//
//        // 3. Then: 결과 출력
//        printJson("주변 주유소 목록", result);
//        assertThat(result).isNotNull();
//    }
//
////    @Test
////    @DisplayName("주유소 상세 정보 실제 호출 및 콘솔 출력")
////    void printDetailGasStation() throws Exception {
////        // 1. Given: 실제 주유소 ID
////        String stationId = "A0011826";
////
////        // 2. When: 실제 API 호출
////        OpinetDetailDto detail = gasStationApiClient.getDetailGasStation(stationId);
////
////        // 3. Then: 결과 출력
////        printJson("주유소 상세 정보", detail);
////        assertThat(detail).isNotNull();
////    }
//
//    // JSON 출력을 위한 헬퍼 메소드
//    private void printJson(String title, Object obj) throws Exception {
//        System.out.println("\n==================== [" + title + "] ====================");
//        if (obj != null) {
//            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
//        } else {
//            System.out.println("데이터가 비어있습니다 (null).");
//        }
//        System.out.println("==========================================================");
//    }
//}