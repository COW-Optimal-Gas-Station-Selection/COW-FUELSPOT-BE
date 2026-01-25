package com.cow.fuelspot.domain.favorite.dto;

import com.cow.fuelspot.domain.favorite.entity.Favorite;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoriteResponse {

    @Schema(description = "즐겨찾기 고유 ID (PK)", example = "1")
    private Long favoriteId;

    @Schema(description = "오피넷 주유소 ID", example = "A0001234")
    private String stationId;

    @Builder
    public FavoriteResponse(Long favoriteId, String stationId){
        this.favoriteId = favoriteId;
        this.stationId = stationId;
    }

    public  static  FavoriteResponse from(Favorite favorite){
        return FavoriteResponse.builder()
                .favoriteId(favorite.getId())
                .stationId(favorite.getStationId())
                .build();
    }
}
