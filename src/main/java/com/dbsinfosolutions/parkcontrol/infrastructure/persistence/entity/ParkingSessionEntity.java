package com.dbsinfosolutions.parkcontrol.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;

import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSessionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "parking_session")
public class ParkingSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String plate;

    @Column(name = "spot_id", nullable = false)
    private Long spotId;

    @Column(nullable = false)
    private String sector;

    @Column(name = "entry_time", nullable = false)
    private Instant entryTime;

    @Column(name = "parked_time")
    private Instant parkedTime;

    @Column(name = "exit_time")
    private Instant exitTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParkingSessionStatus status;

    @Column(name = "amount_paid", precision = 19, scale = 2, nullable = false)
    private BigDecimal amountPaid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Instant getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Instant entryTime) {
        this.entryTime = entryTime;
    }

    public Instant getParkedTime() {
        return parkedTime;
    }

    public void setParkedTime(Instant parkedTime) {
        this.parkedTime = parkedTime;
    }

    public Instant getExitTime() {
        return exitTime;
    }

    public void setExitTime(Instant exitTime) {
        this.exitTime = exitTime;
    }

    public ParkingSessionStatus getStatus() {
        return status;
    }

    public void setStatus(ParkingSessionStatus status) {
        this.status = status;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
}
