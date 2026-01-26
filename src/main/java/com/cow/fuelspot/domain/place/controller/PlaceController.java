package com.cow.fuelspot.domain.place.controller;

import com.cow.fuelspot.domain.place.dto.PlaceResponse;
import com.cow.fuelspot.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/place")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;
    //키워드로 장소 검색
    @GetMapping("/search")
    public ResponseEntity<PlaceResponse> searchPlaces(String keyword) {
        PlaceResponse response = placeService.searchNearbyPlaces(keyword);
        return ResponseEntity.ok(response);
    }
}

