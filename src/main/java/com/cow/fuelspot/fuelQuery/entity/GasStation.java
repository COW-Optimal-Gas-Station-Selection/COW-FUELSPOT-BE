package com.cow.fuelspot.fuelQuery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "gas_station")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GasStation {

    @Id
    @Column(name = "station_id", length = 20)
    private String stationId; //

    private String name; //
    private String brand; //
    private String address;
    private Double lat;
    private Double lon;

    @Column(name = "price_gasoline")
    private Integer priceGasoline; //

    @Column(name = "price_diesel")
    private Integer priceDiesel; //

    private Double distance;

    private Boolean isSelf; //
    private Boolean isCarWash; //
}


