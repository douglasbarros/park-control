package com.dbsinfosolutions.parkcontrol.domain.repository;

import java.util.List;
import java.util.Optional;

import com.dbsinfosolutions.parkcontrol.domain.model.Garage;

public interface GarageRepository {

    List<Garage> saveAll(List<Garage> garages);

    List<Garage> findAll();

    Optional<Garage> findBySector(String sector);

    Integer sumTotalCapacity();
}
