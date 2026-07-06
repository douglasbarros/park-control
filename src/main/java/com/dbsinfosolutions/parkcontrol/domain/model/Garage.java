package com.dbsinfosolutions.parkcontrol.domain.model;

import java.math.BigDecimal;

public class Garage {

    private Long id;
    private final String sector;
    private final BigDecimal basePrice;
    private final Integer capacity;
    private final String openHour;
    private final String closeHour;
    private final Integer durationLimitMinutes;

    public Garage(Long id, String sector, BigDecimal basePrice, Integer capacity, String openHour,
            String closeHour, Integer durationLimitMinutes) {
        this.id = id;
        this.sector = sector;
        this.basePrice = basePrice;
        this.capacity = capacity;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.durationLimitMinutes = durationLimitMinutes;
    }

    public Garage(Long id, String sector, BigDecimal basePrice, Integer capacity) {
        this(id, sector, basePrice, capacity, null, null, null);
    }

    public static Garage create(String sector, BigDecimal basePrice, Integer capacity) {
        return new Garage(null, sector, basePrice, capacity, null, null, null);
    }

    public static Garage create(String sector, BigDecimal basePrice, Integer capacity,
            String openHour, String closeHour, Integer durationLimitMinutes) {
        return new Garage(null, sector, basePrice, capacity, openHour, closeHour,
                durationLimitMinutes);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSector() {
        return sector;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getOpenHour() {
        return openHour;
    }

    public String getCloseHour() {
        return closeHour;
    }

    public Integer getDurationLimitMinutes() {
        return durationLimitMinutes;
    }
}
