package com.cow.fuelspot.domain.favorite.repository;

import com.cow.fuelspot.domain.favorite.entity.Favorite;
import com.cow.fuelspot.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {


    List<Favorite> findAllByMember(Member member);

    boolean existsByMemberAndStationId(Member member, String stationId);

    void deleteByMemberAndStationId(Member member, String stationId);

    long countByStationId(String stationId);
}
