package com.dbsinfosolutions.parkcontrol.domain.model;

import java.math.BigDecimal;

public class Spot {

    private Long id;
    private final String sector;
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private SpotStatus status;

    public Spot(Long id, String sector, BigDecimal latitude, BigDecimal longitude,
            SpotStatus status) {
        this.id = id;
        this.sector = sector;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public static Spot create(String sector, BigDecimal latitude, BigDecimal longitude) {
        return new Spot(null, sector, latitude, longitude, SpotStatus.FREE);
    }

    public static Spot create(String sector, BigDecimal latitude, BigDecimal longitude,
            boolean occupied) {
        return new Spot(null, sector, latitude, longitude,
                occupied ? SpotStatus.OCCUPIED : SpotStatus.FREE);
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

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public SpotStatus getStatus() {
        return status;
    }

    public void occupy() {
        this.status = SpotStatus.OCCUPIED;
    }

    public void release() {
        this.status = SpotStatus.FREE;
    }
}
