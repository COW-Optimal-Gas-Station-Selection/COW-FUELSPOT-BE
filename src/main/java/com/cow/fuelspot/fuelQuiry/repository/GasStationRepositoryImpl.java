package com.cow.fuelspot.fuelQuiry.repository;

import com.cow.fuelspot.fuelQuiry.dto.FuelType;
import com.cow.fuelspot.fuelQuiry.dto.GasStationDto;
import com.cow.fuelspot.fuelQuiry.dto.GasStationRequest;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.cow.fuelspot.fuelQuiry.entity.QGasStation.gasStation;

@RequiredArgsConstructor
public class GasStationRepositoryImpl implements GasStationRepositoryCustom {

    private final JPAQueryFactory queryFactory; // QuerydslConfig에서 등록한 빈 주입

    @Override
    public List<GasStationDto> findNearbyStations(GasStationRequest request) {

        NumberPath<Integer> pricePath = (request.getFuelType() == FuelType.GASOLINE)
                ? gasStation.priceGasoline : gasStation.priceDiesel;

        return queryFactory
                .select(Projections.constructor(GasStationDto.class,
                        gasStation.stationId,
                        gasStation.name,
                        gasStation.brand,
                        pricePath,           // DTO의 price 필드로 매핑
                        gasStation.distance,
                        gasStation.lat,
                        gasStation.lon
                ))
                .from(gasStation)
                .where(
                        gasStation.distance.loe(request.getRadius()), // 반경 필터
                        brandEq(request.getBrand()),
                        isSelfEq(request.getIsSelf()),
                        isCarWashEq(request.getIsCarWash())
                )
                .orderBy(request.getSort() == 1 ? gasStation.distance.asc() : pricePath.asc()) // 정렬
                .fetch();
    }


    private BooleanExpression brandEq(String brand) {
        return StringUtils.hasText(brand) ? gasStation.brand.eq(brand) : null;
    }

    private BooleanExpression isSelfEq(Boolean isSelf) {
        return isSelf != null ? gasStation.isSelf.eq(isSelf) : null;
    }

    private BooleanExpression isCarWashEq(Boolean isCarWash) {
        return isCarWash != null ? gasStation.isCarWash.eq(isCarWash) : null;
    }
}