package com.dbsinfosolutions.parkcontrol.infrastructure.persistence.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dbsinfosolutions.parkcontrol.domain.model.Spot;
import com.dbsinfosolutions.parkcontrol.domain.model.SpotStatus;
import com.dbsinfosolutions.parkcontrol.domain.repository.SpotRepository;
import com.dbsinfosolutions.parkcontrol.infrastructure.persistence.mapper.SpotMapper;

@Repository
public class SpotRepositoryAdapter implements SpotRepository {

    private final SpotJpaRepository spotJpaRepository;
    private final SpotMapper spotMapper;

    public SpotRepositoryAdapter(SpotJpaRepository spotJpaRepository, SpotMapper spotMapper) {
        this.spotJpaRepository = spotJpaRepository;
        this.spotMapper = spotMapper;
    }

    @Override
    public List<Spot> saveAll(List<Spot> spots) {
        return spotJpaRepository.saveAll(spots.stream().map(spotMapper::toEntity).toList()).stream()
                .map(spotMapper::toDomain).toList();
    }

    @Override
    public Spot save(Spot spot) {
        return spotMapper.toDomain(spotJpaRepository.save(spotMapper.toEntity(spot)));
    }

    @Override
    public Optional<Spot> findFirstFreeForUpdate() {
        return spotJpaRepository.findFirstByStatusOrderByIdAsc(SpotStatus.FREE)
                .map(spotMapper::toDomain);
    }

    @Override
    public Optional<Spot> findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude) {
        return spotJpaRepository.findByLatitudeAndLongitude(latitude, longitude)
                .map(spotMapper::toDomain);
    }

    @Override
    public Optional<Spot> findById(Long id) {
        return spotJpaRepository.findById(id).map(spotMapper::toDomain);
    }

    @Override
    public long countByStatus(SpotStatus status) {
        return spotJpaRepository.countByStatus(status);
    }

    @Override
    public long countAll() {
        return spotJpaRepository.count();
    }
}
