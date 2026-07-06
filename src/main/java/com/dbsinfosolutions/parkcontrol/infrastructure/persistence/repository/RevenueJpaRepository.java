package com.dbsinfosolutions.parkcontrol.infrastructure.persistence.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbsinfosolutions.parkcontrol.infrastructure.persistence.entity.RevenueEntity;

public interface RevenueJpaRepository extends JpaRepository<RevenueEntity, Long> {

    Optional<RevenueEntity> findByDateAndSector(LocalDate date, String sector);
}
