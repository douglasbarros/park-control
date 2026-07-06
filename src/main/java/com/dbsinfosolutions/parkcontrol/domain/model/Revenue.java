package com.dbsinfosolutions.parkcontrol.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Revenue {

    private Long id;
    private final LocalDate date;
    private final String sector;
    private BigDecimal amount;

    public Revenue(Long id, LocalDate date, String sector, BigDecimal amount) {
        this.id = id;
        this.date = date;
        this.sector = sector;
        this.amount = amount;
    }

    public static Revenue create(LocalDate date, String sector, BigDecimal amount) {
        return new Revenue(null, date, sector, amount);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSector() {
        return sector;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void addAmount(BigDecimal amountToAdd) {
        this.amount = this.amount.add(amountToAdd);
    }
}
