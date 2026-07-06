package com.dbsinfosolutions.parkcontrol.domain.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.dbsinfosolutions.parkcontrol.domain.model.Revenue;

public interface RevenueRepository {

    Revenue save(Revenue revenue);

    Optional<Revenue> findByDateAndSector(LocalDate date, String sector);
}
