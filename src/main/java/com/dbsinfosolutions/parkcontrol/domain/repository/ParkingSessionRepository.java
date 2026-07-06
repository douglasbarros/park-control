package com.dbsinfosolutions.parkcontrol.domain.repository;

import java.util.Optional;

import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSession;

public interface ParkingSessionRepository {

    ParkingSession save(ParkingSession parkingSession);

    Optional<ParkingSession> findOpenByPlate(String plate);

    Optional<ParkingSession> findEntryByPlate(String plate);
}
