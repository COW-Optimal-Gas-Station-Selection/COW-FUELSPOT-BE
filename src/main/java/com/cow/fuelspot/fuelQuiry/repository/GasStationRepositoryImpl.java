package com.cow.fuelspot.fuelQuiry.repository;

import com.cow.fuelspot.fuelQuiry.dto.GasStationDto;
import com.cow.fuelspot.fuelQuiry.dto.GasStationRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class GasStationRepositoryImpl implements GasStationRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<GasStationDto> findNearbyStations(GasStationRequest request) {

        String priceField = switch (request.getFuelType()) {
            case GASOLINE -> "g.priceGasoline";
            case DIESEL -> "g.priceDiesel";
        };

        StringBuilder jpql = new StringBuilder();
        jpql.append("select new com.cow.fuelspot.fuelQuiry.dto.GasStationDto(")
                .append("g.stationId, ")
                .append("g.name, ")
                .append("g.brand, ")
                .append(priceField).append(", ")
                .append("g.distance, ")
                .append("g.lat, ")
                .append("g.lon")
                .append(") ")
                .append("from GasStation g ")
                .append("where g.distance <= :radius");

        if (StringUtils.hasText(request.getBrand())) {
            jpql.append(" and g.brand = :brand");
        }
        if (request.getIsSelf() != null) {
            jpql.append(" and g.isSelf = :isSelf");
        }
        if (request.getIsCarWash() != null) {
            jpql.append(" and g.isCarWash = :isCarWash");
        }

        if (request.getSort() == 1) {
            jpql.append(" order by g.distance asc");
        } else {
            jpql.append(" order by ").append(priceField).append(" asc");
        }

        TypedQuery<GasStationDto> query = em.createQuery(jpql.toString(), GasStationDto.class);
        query.setParameter("radius", request.getRadius());

        if (StringUtils.hasText(request.getBrand())) {
            query.setParameter("brand", request.getBrand());
        }
        if (request.getIsSelf() != null) {
            query.setParameter("isSelf", request.getIsSelf());
        }
        if (request.getIsCarWash() != null) {
            query.setParameter("isCarWash", request.getIsCarWash());
        }

        return query.getResultList();
    }
}
