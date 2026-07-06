package com.dbsinfosolutions.parkcontrol.infrastructure.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.dbsinfosolutions.parkcontrol.application.usecase.InitializeGarageUseCase;

@Component
public class StartupInitializationListener {

    private static final Logger log = LoggerFactory.getLogger(StartupInitializationListener.class);

    private final InitializeGarageUseCase initializeGarageUseCase;

    public StartupInitializationListener(InitializeGarageUseCase initializeGarageUseCase) {
        this.initializeGarageUseCase = initializeGarageUseCase;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            initializeGarageUseCase.execute();
        } catch (Exception exception) {
            log.warn("Garage initialization skipped: {}", exception.getMessage());
        }
    }
}
