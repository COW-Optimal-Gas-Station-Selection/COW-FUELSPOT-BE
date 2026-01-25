package com.cow.fuelspot.domain.favorite.dto;

import com.cow.fuelspot.domain.favorite.entity.Favorite;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoriteResponse {
    private  Long favoriteId;
    private  String StationId;

    @Builder
    public FavoriteResponse(Long favoriteId, String stationId){
        this.favoriteId = favoriteId;
        this.StationId = stationId;
    }

    public  static  FavoriteResponse from(Favorite favorite){
        return FavoriteResponse.builder()
                .favoriteId(favorite.getId())
                .stationId(favorite.getStationId())
                .build();
    }
}
