package com.dbsinfosolutions.parkcontrol.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbsinfosolutions.parkcontrol.infrastructure.persistence.entity.GarageEntity;

public interface GarageJpaRepository extends JpaRepository<GarageEntity, Long> {

    Optional<GarageEntity> findBySector(String sector);
}
