package com.cow.fuelspot.domain.station.service;

import com.cow.fuelspot.domain.map.dto.KakaoTranscoordResponse;
import com.cow.fuelspot.domain.map.service.KakaoMapService;
import com.cow.fuelspot.global.common.enums.FuelType;
import com.cow.fuelspot.domain.station.Repository.UserRepository;
import com.cow.fuelspot.domain.station.client.GasStationApiClient;
import com.cow.fuelspot.domain.station.component.FuelCalculator;
import com.cow.fuelspot.domain.station.component.OpinetMapper;
import com.cow.fuelspot.domain.station.component.StationFilter;
import com.cow.fuelspot.domain.station.dto.enums.Sido;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetAverageDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetSidoAverageDto;
import com.cow.fuelspot.domain.station.dto.request.NearbyRequest;
import com.cow.fuelspot.domain.station.dto.response.AverageStationResponse;
import com.cow.fuelspot.domain.station.dto.response.DetailResponse;
import com.cow.fuelspot.domain.station.dto.response.NearbyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpinetService {

    private final GasStationApiClient gasStationApiClient;
    private final FuelCalculator fuelCalculator;
    private final StationFilter stationFilter;
    private final OpinetMapper opinetMapper;
    private final UserRepository memberRepository;
    private final KakaoMapService kakaoMapService;

    // 근처 주유소 조회(우선순위 request FuelType<-로그인 유저 타입<-기본값(가솔린))
    public List<NearbyResponse> getNearbyGasStations(NearbyRequest request, Authentication authentication) {
        //좌표: 카카오->오피넷
        KakaoTranscoordResponse ktmResponse = kakaoMapService.convertWGS84ToKTM(
                String.valueOf(request.getLon()),
                String.valueOf(request.getLat())
        );
        if (ktmResponse != null && !ktmResponse.getDocuments().isEmpty()) {
            request.setLat(Double.valueOf(ktmResponse.getDocuments().get(0).getX()));
            request.setLon(Double.valueOf(ktmResponse.getDocuments().get(0).getY()));
        }
        FuelType fuelType = null;
        if(request.getFuelType()==null){
            //유저 정보(선호유종) 조회
            if(authentication!=null) {
                String userId = authentication.getName();
                fuelType = memberRepository.findFuelTypeByMemberId(userId);
            }
            //기본값 설정(가솔린)
            if (fuelType==null){
                fuelType=FuelType.GASOLINE;
            }
        }else{
            fuelType = request.getFuelType();
        }

        List<OpinetNearbyDto> nearbyDtos = gasStationApiClient.getStation(request,fuelType);
        if (nearbyDtos == null) return List.of();

        FuelType finalFuelType = fuelType;

        return nearbyDtos.stream()
                .parallel()
                .map(nearby -> new StationPair(nearby, gasStationApiClient.getDetailGasStation(nearby.getId())))
                .filter(pair -> stationFilter.isMatch(pair.nearby(), pair.detail(), request))
                .map(pair -> opinetMapper.toNearbyResponse(pair.nearby(), pair.detail()))
                .sorted(Comparator.comparingDouble(n -> fuelCalculator.calculateFuelConsumption(n, finalFuelType)))
                .collect(Collectors.toList());
    }

    // 주유소 상세 조회
    public DetailResponse getDetailGasStation(String id) {
        OpinetDetailDto detailDto = gasStationApiClient.getDetailGasStation(id);
        return opinetMapper.toDetailResponse(detailDto);
    }


    // 전국 평균 유가 조회
    public AverageStationResponse getAverageStation() {
        List<OpinetAverageDto> dtos = gasStationApiClient.getAverageGasStation();

        Map<FuelType, AverageStationResponse.AveragePriceInfo> prices = new HashMap<>();

        for (FuelType fuelType : FuelType.values()) {
            Integer average = opinetMapper.extractAveragePrice(dtos, fuelType);
            Double weeklyChange = opinetMapper.extractWeeklyChange(dtos, fuelType);

            prices.put(fuelType, AverageStationResponse.AveragePriceInfo.builder()
                    .average(average)
                    .weeklyChange(weeklyChange)
                    .build()
            );
        }


        return AverageStationResponse.of(prices);
    }

    //시도별 평균 유가 조회
    public AverageStationResponse getSidoAverageStation(Sido sido) {
        List<OpinetSidoAverageDto> dtos = gasStationApiClient.getsidoAverageGasStation(sido);

        Map<FuelType, AverageStationResponse.AveragePriceInfo> prices = new HashMap<>();

        for (FuelType fuelType : FuelType.values()) {
            Integer average = opinetMapper.extractSidoAveragePrice(dtos, fuelType);
            Double weeklyChange = opinetMapper.extractSidoWeeklyChange(dtos, fuelType);

            prices.put(fuelType, AverageStationResponse.AveragePriceInfo.builder()
                    .average(average)
                    .weeklyChange(weeklyChange)
                    .build()
            );
        }
        return AverageStationResponse.of(prices);
    }

    private record StationPair(OpinetNearbyDto nearby, OpinetDetailDto detail) {}
}