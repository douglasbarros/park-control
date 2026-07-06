package com.dbsinfosolutions.parkcontrol.infrastructure.persistence.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dbsinfosolutions.parkcontrol.domain.model.Revenue;
import com.dbsinfosolutions.parkcontrol.domain.repository.RevenueRepository;
import com.dbsinfosolutions.parkcontrol.infrastructure.persistence.mapper.RevenueMapper;

@Repository
public class RevenueRepositoryAdapter implements RevenueRepository {

    private final RevenueJpaRepository revenueJpaRepository;
    private final RevenueMapper revenueMapper;

    public RevenueRepositoryAdapter(RevenueJpaRepository revenueJpaRepository,
            RevenueMapper revenueMapper) {
        this.revenueJpaRepository = revenueJpaRepository;
        this.revenueMapper = revenueMapper;
    }

    @Override
    public Revenue save(Revenue revenue) {
        return revenueMapper.toDomain(revenueJpaRepository.save(revenueMapper.toEntity(revenue)));
    }

    @Override
    public Optional<Revenue> findByDateAndSector(LocalDate date, String sector) {
        return revenueJpaRepository.findByDateAndSector(date, sector).map(revenueMapper::toDomain);
    }
}
