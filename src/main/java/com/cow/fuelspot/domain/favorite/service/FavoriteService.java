package com.cow.fuelspot.domain.favorite.service;

import com.cow.fuelspot.domain.favorite.dto.FavoriteRequest;
import com.cow.fuelspot.domain.favorite.dto.FavoriteResponse;
import com.cow.fuelspot.domain.favorite.entity.Favorite;
import com.cow.fuelspot.domain.favorite.repository.FavoriteRepository;
import com.cow.fuelspot.domain.member.entity.Member;
import com.cow.fuelspot.domain.member.repository.MemberRepository;
import com.cow.fuelspot.global.common.code.ErrorCode;
import com.cow.fuelspot.global.common.exception.CustomException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public FavoriteResponse addFavorite(String email, String stationId) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (favoriteRepository.existsByMemberAndStationId(member, stationId)) {
            throw new CustomException(ErrorCode.DUPLICATE_FAVORITE);
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
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        favoriteRepository.deleteByMemberAndStationId(member, stationId);
    }

    public List<FavoriteResponse> getFavorites(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return favoriteRepository.findAllByMember(member)
                .stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    public long getFavoriteCount(String stationId) {
        return favoriteRepository.countByStationId(stationId);
    }
}
