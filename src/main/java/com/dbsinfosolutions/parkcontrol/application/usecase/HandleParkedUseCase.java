package com.dbsinfosolutions.parkcontrol.application.usecase;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbsinfosolutions.parkcontrol.domain.exception.SpotNotFoundException;
import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSession;
import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSessionStatus;
import com.dbsinfosolutions.parkcontrol.domain.model.Spot;
import com.dbsinfosolutions.parkcontrol.domain.repository.ParkingSessionRepository;
import com.dbsinfosolutions.parkcontrol.domain.repository.SpotRepository;

@Service
public class HandleParkedUseCase {

    private final ParkingSessionRepository parkingSessionRepository;
    private final SpotRepository spotRepository;

    public HandleParkedUseCase(ParkingSessionRepository parkingSessionRepository,
            SpotRepository spotRepository) {
        this.parkingSessionRepository = parkingSessionRepository;
        this.spotRepository = spotRepository;
    }

    @Transactional
    public void execute(String plate, BigDecimal latitude, BigDecimal longitude,
            Instant parkedTime) {
        ParkingSession session = parkingSessionRepository.findEntryByPlate(plate).orElse(null);
        if (session == null) {
            return;
        }
        if (session.getStatus() != ParkingSessionStatus.ENTRY) {
            return;
        }
        Spot spot = spotRepository.findByLatitudeAndLongitude(latitude, longitude)
                .orElseThrow(SpotNotFoundException::new);
        if (!spot.getId().equals(session.getSpotId())) {
            return;
        }

        session.markParked(parkedTime);
        parkingSessionRepository.save(session);
    }
}
