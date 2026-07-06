package com.dbsinfosolutions.parkcontrol.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSession;
import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSessionStatus;
import com.dbsinfosolutions.parkcontrol.domain.repository.ParkingSessionRepository;
import com.dbsinfosolutions.parkcontrol.infrastructure.persistence.mapper.ParkingSessionMapper;

@Repository
public class ParkingSessionRepositoryAdapter implements ParkingSessionRepository {

    private final ParkingSessionJpaRepository parkingSessionJpaRepository;
    private final ParkingSessionMapper parkingSessionMapper;

    public ParkingSessionRepositoryAdapter(ParkingSessionJpaRepository parkingSessionJpaRepository,
            ParkingSessionMapper parkingSessionMapper) {
        this.parkingSessionJpaRepository = parkingSessionJpaRepository;
        this.parkingSessionMapper = parkingSessionMapper;
    }

    @Override
    public ParkingSession save(ParkingSession parkingSession) {
        return parkingSessionMapper.toDomain(
                parkingSessionJpaRepository.save(parkingSessionMapper.toEntity(parkingSession)));
    }

    @Override
    public Optional<ParkingSession> findOpenByPlate(String plate) {
        return parkingSessionJpaRepository
                .findFirstByPlateAndStatusInOrderByEntryTimeDesc(plate,
                        List.of(ParkingSessionStatus.ENTRY, ParkingSessionStatus.PARKED))
                .map(parkingSessionMapper::toDomain);
    }

    @Override
    public Optional<ParkingSession> findEntryByPlate(String plate) {
        return parkingSessionJpaRepository
                .findFirstByPlateAndStatusOrderByEntryTimeDesc(plate, ParkingSessionStatus.ENTRY)
                .map(parkingSessionMapper::toDomain);
    }
}
