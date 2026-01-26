package com.cow.fuelspot.domain.favorite.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoriteRequest {

    @Schema(description = "오피넷 주유소 ID", example = "A0001234")
    private String stationId;
}