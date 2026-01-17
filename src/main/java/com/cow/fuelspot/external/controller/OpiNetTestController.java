package com.cow.fuelspot.external.controller;

import com.cow.fuelspot.external.client.OpiNetApiClient;
import com.cow.fuelspot.external.dto.OpiNetAroundResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/opinet")
public class OpiNetTestController {

    private final OpiNetApiClient opiNetApiClient;

    public OpiNetTestController(OpiNetApiClient opiNetApiClient) {
        this.opiNetApiClient = opiNetApiClient;
    }

    @GetMapping("/around")
    public OpiNetAroundResponseDTO around(
            @RequestParam double x,
            @RequestParam double y,
            @RequestParam(defaultValue = "1000") int radius,
            @RequestParam(defaultValue = "B027") String prodCd
    ) {
        return opiNetApiClient.fetchAroundStations(x, y, radius, prodCd);
    }
}