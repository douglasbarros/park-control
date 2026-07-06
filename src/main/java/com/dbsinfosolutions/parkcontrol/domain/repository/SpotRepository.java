package com.dbsinfosolutions.parkcontrol.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.dbsinfosolutions.parkcontrol.domain.model.Spot;
import com.dbsinfosolutions.parkcontrol.domain.model.SpotStatus;

public interface SpotRepository {

    List<Spot> saveAll(List<Spot> spots);

    Spot save(Spot spot);

    Optional<Spot> findFirstFreeForUpdate();

    Optional<Spot> findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);

    Optional<Spot> findById(Long id);

    long countByStatus(SpotStatus status);

    long countAll();
}
