package com.dbsinfosolutions.parkcontrol.infrastructure.persistence.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.dbsinfosolutions.parkcontrol.domain.model.SpotStatus;
import com.dbsinfosolutions.parkcontrol.infrastructure.persistence.entity.SpotEntity;

import jakarta.persistence.LockModeType;

public interface SpotJpaRepository extends JpaRepository<SpotEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SpotEntity> findFirstByStatusOrderByIdAsc(SpotStatus status);

    Optional<SpotEntity> findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);

    long countByStatus(SpotStatus status);
}
