package com.dbsinfosolutions.parkcontrol.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dbsinfosolutions.parkcontrol.domain.model.Garage;
import com.dbsinfosolutions.parkcontrol.domain.repository.GarageRepository;
import com.dbsinfosolutions.parkcontrol.infrastructure.persistence.mapper.GarageMapper;

@Repository
public class GarageRepositoryAdapter implements GarageRepository {

    private final GarageJpaRepository garageJpaRepository;
    private final GarageMapper garageMapper;

    public GarageRepositoryAdapter(GarageJpaRepository garageJpaRepository,
            GarageMapper garageMapper) {
        this.garageJpaRepository = garageJpaRepository;
        this.garageMapper = garageMapper;
    }

    @Override
    public List<Garage> saveAll(List<Garage> garages) {
        return garageJpaRepository.saveAll(garages.stream().map(garageMapper::toEntity).toList())
                .stream().map(garageMapper::toDomain).toList();
    }

    @Override
    public List<Garage> findAll() {
        return garageMapper.toDomainList(garageJpaRepository.findAll());
    }

    @Override
    public Optional<Garage> findBySector(String sector) {
        return garageJpaRepository.findBySector(sector).map(garageMapper::toDomain);
    }
}
