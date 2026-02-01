package com.cow.fuelspot.domain.favorite.service;

import com.cow.fuelspot.domain.favorite.dto.FavoriteResponse;
import com.cow.fuelspot.domain.favorite.entity.Favorite;
import com.cow.fuelspot.domain.favorite.repository.FavoriteRepository;
import com.cow.fuelspot.domain.member.entity.Member;
import com.cow.fuelspot.domain.member.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public FavoriteResponse addFavorite(String email, String stationId) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        if (favoriteRepository.existsByMemberAndStationId(member, stationId)) {
            throw new IllegalStateException("이미 즐겨찾기 된 주유소입니다.");
        }

        Favorite favorite = Favorite.builder()
                .member(member)
                .stationId(stationId)
                .build();

        favoriteRepository.save(favorite);

        return FavoriteResponse.from(favorite);
    }

    @Transactional
    public void removeFavorite(String email, String stationId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        favoriteRepository.deleteByMemberAndStationId(member, stationId);
    }

    public long getFavoriteCount(String stationId) {
        return favoriteRepository.countByStationId(stationId);
    }
}
