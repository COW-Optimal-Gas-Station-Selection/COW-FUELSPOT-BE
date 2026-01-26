package com.cow.fuelspot.domain.place.service;

import com.cow.fuelspot.domain.place.client.PlaceClient;
import com.cow.fuelspot.domain.place.dto.PlaceDto;
import com.cow.fuelspot.domain.place.dto.PlaceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceClient client;

    public PlaceResponse searchNearbyPlaces(String keyword) {
        PlaceDto placeDto = client.searchPlaces(keyword);
        return convertToResponse(placeDto);
    }
    //placeResponse로 전환
    private PlaceResponse convertToResponse(PlaceDto placeDto) {
        if (placeDto == null || placeDto.getPlaces() == null) {
            return PlaceResponse.builder()
                    .places(List.of())
                    .hasMore(false)
                    .build();
        }

        List<PlaceResponse.Place> places = placeDto.getPlaces().stream()
                .map(info -> PlaceResponse.Place.builder()
                        .id(info.getId())
                        .name(info.getName())
                        .address(info.getAddress())
                        .category(extractTargetCategory(info.getCategory(),info.getTag()))
                        .phone(info.getPhone())
                        .lat(info.getLat())
                        .lon(info.getLon())
                        .build())
                .toList();

        return PlaceResponse.builder()
                .places(places)
                .hasMore(!placeDto.isEnd())
                .build();
    }
    //키워드 정렬
    private String extractTargetCategory(String category, String tag) {
        if (category == null || category.isBlank()) {
            return "";
        }
        if(!tag.equals("")) return tag;
        String[] parts = category.split(" > ");
        int length = parts.length;

        if (length >= 3) {
            return parts[length - 3];
        } else if (length > 0) {
            return parts[length - 1];
        }

        return "";
    }
}
