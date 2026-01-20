//package com.cow.fuelspot.domain.station.dto.request;
//
//import com.cow.fuelspot.domain.station.dto.enums.FuelType;
//import com.cow.fuelspot.domain.station.dto.enums.SortType;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.Setter;
//
//public class FilterGasStationRequest {
//
//    @Getter
//    @Setter
//    @Builder
//    @AllArgsConstructor
//    public static class GasStationRequest {
//        private Double lat;          // 위도
//        private Double lon;          // 경도
//        private Double radius;       // 반경
//        private Integer sort;        // 정렬 코드 (1, 2, 3)
//        private FuelType fuelType;   // 유종 Enum
//        private String brand;        // 브랜드
//        private Boolean isSelf;      // 셀프 여부
//        private Boolean isCarWash;   // 세차장 여부
//
//        public SortType getSortType() {
//            return SortType.fromCode(this.sort);
//        }
//
//    }
//
//    @Builder
//    @Getter
//    public static class NearbyGasStationRequest {
//        String lat;
//        String lon;
//        Integer radius;
//        FuelType fuelType;
//        Integer sortType;
//    }
//}
