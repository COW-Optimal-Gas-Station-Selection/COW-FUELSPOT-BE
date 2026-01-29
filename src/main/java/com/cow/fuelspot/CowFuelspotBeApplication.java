package com.cow.fuelspot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CowFuelspotBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CowFuelspotBeApplication.class, args);
    }

}
