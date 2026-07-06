package com.dbsinfosolutions.parkcontrol.infrastructure.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSessionStatus;
import com.dbsinfosolutions.parkcontrol.infrastructure.persistence.entity.ParkingSessionEntity;

public interface ParkingSessionJpaRepository extends JpaRepository<ParkingSessionEntity, Long> {

    Optional<ParkingSessionEntity> findFirstByPlateAndStatusInOrderByEntryTimeDesc(String plate,
            Collection<ParkingSessionStatus> statuses);

    Optional<ParkingSessionEntity> findFirstByPlateAndStatusOrderByEntryTimeDesc(String plate,
            ParkingSessionStatus status);
}
