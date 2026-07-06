package com.dbsinfosolutions.parkcontrol.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public class ParkingSession {

    private Long id;
    private final String plate;
    private final Long spotId;
    private final String sector;
    private final Instant entryTime;
    private Instant parkedTime;
    private Instant exitTime;
    private ParkingSessionStatus status;
    private BigDecimal amountPaid;

    public ParkingSession(Long id, String plate, Long spotId, String sector, Instant entryTime,
            Instant parkedTime, Instant exitTime, ParkingSessionStatus status,
            BigDecimal amountPaid) {
        this.id = id;
        this.plate = plate;
        this.spotId = spotId;
        this.sector = sector;
        this.entryTime = entryTime;
        this.parkedTime = parkedTime;
        this.exitTime = exitTime;
        this.status = status;
        this.amountPaid = amountPaid;
    }

    public static ParkingSession create(String plate, Long spotId, String sector,
            Instant entryTime) {
        return new ParkingSession(null, plate, spotId, sector, entryTime, null, null,
                ParkingSessionStatus.ENTRY, BigDecimal.ZERO);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public Long getSpotId() {
        return spotId;
    }

    public String getSector() {
        return sector;
    }

    public Instant getEntryTime() {
        return entryTime;
    }

    public Instant getParkedTime() {
        return parkedTime;
    }

    public Instant getExitTime() {
        return exitTime;
    }

    public ParkingSessionStatus getStatus() {
        return status;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void markParked(Instant parkedTime) {
        this.parkedTime = parkedTime;
        this.status = ParkingSessionStatus.PARKED;
    }

    public void close(Instant exitTime, BigDecimal amountPaid) {
        this.exitTime = exitTime;
        this.amountPaid = amountPaid;
        this.status = ParkingSessionStatus.EXIT;
    }
}
