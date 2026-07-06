package com.dbsinfosolutions.parkcontrol.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbsinfosolutions.parkcontrol.domain.model.Garage;
import com.dbsinfosolutions.parkcontrol.domain.model.GarageCatalog;
import com.dbsinfosolutions.parkcontrol.domain.model.Spot;
import com.dbsinfosolutions.parkcontrol.domain.repository.GarageCatalogClient;
import com.dbsinfosolutions.parkcontrol.domain.repository.GarageRepository;
import com.dbsinfosolutions.parkcontrol.domain.repository.SpotRepository;

@Service
public class InitializeGarageUseCase {

    private final GarageCatalogClient garageCatalogClient;
    private final GarageRepository garageRepository;
    private final SpotRepository spotRepository;

    public InitializeGarageUseCase(GarageCatalogClient garageCatalogClient,
            GarageRepository garageRepository, SpotRepository spotRepository) {
        this.garageCatalogClient = garageCatalogClient;
        this.garageRepository = garageRepository;
        this.spotRepository = spotRepository;
    }

    @Transactional
    public void execute() {
        if (!garageRepository.findAll().isEmpty()) {
            return;
        }

        List<GarageCatalog> catalog = garageCatalogClient.fetchGarages();
        List<Garage> garages =
                catalog.stream()
                        .map(item -> Garage.create(item.sector(), item.basePrice(), item.capacity(),
                                item.openHour(), item.closeHour(), item.durationLimitMinutes()))
                        .toList();
        garageRepository.saveAll(garages);

        List<Spot> spots =
                catalog.stream()
                        .flatMap(item -> item.spots().stream()
                                .map(spot -> Spot.create(spot.sector(), spot.latitude(),
                                        spot.longitude(), Boolean.TRUE.equals(spot.occupied()))))
                        .toList();
        spotRepository.saveAll(spots);
    }
}
