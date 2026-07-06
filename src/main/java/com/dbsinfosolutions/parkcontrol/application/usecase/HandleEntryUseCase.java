package com.dbsinfosolutions.parkcontrol.application.usecase;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbsinfosolutions.parkcontrol.domain.exception.GarageFullException;
import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSession;
import com.dbsinfosolutions.parkcontrol.domain.model.Spot;
import com.dbsinfosolutions.parkcontrol.domain.repository.GarageRepository;
import com.dbsinfosolutions.parkcontrol.domain.repository.ParkingSessionRepository;
import com.dbsinfosolutions.parkcontrol.domain.repository.SpotRepository;

@Service
public class HandleEntryUseCase {

    private final SpotRepository spotRepository;
    private final ParkingSessionRepository parkingSessionRepository;
    private final GarageRepository garageRepository;

    public HandleEntryUseCase(SpotRepository spotRepository,
            ParkingSessionRepository parkingSessionRepository, GarageRepository garageRepository) {
        this.spotRepository = spotRepository;
        this.parkingSessionRepository = parkingSessionRepository;
        this.garageRepository = garageRepository;
    }

    @Transactional
    public void execute(String plate, Instant entryTime) {
        Optional<ParkingSession> openSession = parkingSessionRepository.findOpenByPlate(plate);
        if (openSession.isPresent()) {
            return;
        }

        Spot spot = spotRepository.findFirstFreeForUpdate().orElseThrow(GarageFullException::new);
        spot.occupy();
        spotRepository.save(spot);

        ParkingSession session =
                ParkingSession.create(plate, spot.getId(), spot.getSector(), entryTime);
        parkingSessionRepository.save(session);
    }

    public long capacity() {
        return garageRepository.findAll().stream().mapToLong(garage -> garage.getCapacity()).sum();
    }
}
