package com.dbsinfosolutions.parkcontrol.application.usecase;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbsinfosolutions.parkcontrol.domain.model.Garage;
import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSession;
import com.dbsinfosolutions.parkcontrol.domain.model.Revenue;
import com.dbsinfosolutions.parkcontrol.domain.model.Spot;
import com.dbsinfosolutions.parkcontrol.domain.model.SpotStatus;
import com.dbsinfosolutions.parkcontrol.domain.repository.GarageRepository;
import com.dbsinfosolutions.parkcontrol.domain.repository.ParkingSessionRepository;
import com.dbsinfosolutions.parkcontrol.domain.repository.RevenueRepository;
import com.dbsinfosolutions.parkcontrol.domain.repository.SpotRepository;
import com.dbsinfosolutions.parkcontrol.domain.service.PricingService;

@Service
public class HandleExitUseCase {

    private final ParkingSessionRepository parkingSessionRepository;
    private final SpotRepository spotRepository;
    private final RevenueRepository revenueRepository;
    private final GarageRepository garageRepository;
    private final PricingService pricingService;

    public HandleExitUseCase(ParkingSessionRepository parkingSessionRepository,
            SpotRepository spotRepository, RevenueRepository revenueRepository,
            GarageRepository garageRepository, PricingService pricingService) {
        this.parkingSessionRepository = parkingSessionRepository;
        this.spotRepository = spotRepository;
        this.revenueRepository = revenueRepository;
        this.garageRepository = garageRepository;
        this.pricingService = pricingService;
    }

    @Transactional
    public void execute(String plate, Instant exitTime) {
        ParkingSession session = parkingSessionRepository.findOpenByPlate(plate).orElse(null);
        if (session == null) {
            return;
        }
        Spot spot = spotRepository.findById(session.getSpotId())
                .orElseThrow(() -> new IllegalStateException("Spot not found for session"));
        Garage garage = garageRepository.findBySector(session.getSector())
                .orElseThrow(() -> new IllegalStateException("Garage not found for session"));

        long occupiedCount = spotRepository.countByStatus(SpotStatus.OCCUPIED);
        int totalCapacity = garageRepository.sumTotalCapacity();
        double occupancyRate = totalCapacity == 0 ? 0d : (double) occupiedCount / totalCapacity;
        BigDecimal amount = pricingService.calculateAmount(Duration.between(session.getEntryTime(), exitTime),
                garage.getBasePrice(), occupancyRate);

        spot.release();
        spotRepository.save(spot);

        session.close(exitTime, amount);
        parkingSessionRepository.save(session);

        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            LocalDate date = exitTime.atZone(ZoneOffset.UTC).toLocalDate();
            Revenue revenue = revenueRepository.findByDateAndSector(date, session.getSector())
                    .orElseGet(() -> Revenue.create(date, session.getSector(), BigDecimal.ZERO));
            revenue.addAmount(amount);
            revenueRepository.save(revenue);
        }
    }
}
