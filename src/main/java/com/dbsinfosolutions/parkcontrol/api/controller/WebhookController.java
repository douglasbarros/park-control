package com.dbsinfosolutions.parkcontrol.api.controller;

import java.time.Instant;
import java.time.ZoneOffset;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbsinfosolutions.parkcontrol.application.dto.WebhookEventRequest;
import com.dbsinfosolutions.parkcontrol.application.usecase.HandleEntryUseCase;
import com.dbsinfosolutions.parkcontrol.application.usecase.HandleExitUseCase;
import com.dbsinfosolutions.parkcontrol.application.usecase.HandleParkedUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/webhook")
@Validated
public class WebhookController {

    private final HandleEntryUseCase handleEntryUseCase;
    private final HandleParkedUseCase handleParkedUseCase;
    private final HandleExitUseCase handleExitUseCase;

    public WebhookController(HandleEntryUseCase handleEntryUseCase,
            HandleParkedUseCase handleParkedUseCase, HandleExitUseCase handleExitUseCase) {
        this.handleEntryUseCase = handleEntryUseCase;
        this.handleParkedUseCase = handleParkedUseCase;
        this.handleExitUseCase = handleExitUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> receive(@Valid @RequestBody WebhookEventRequest request) {
        String eventType = request.eventType().trim().toUpperCase();
        switch (eventType) {
            case "ENTRY" -> handleEntryUseCase.execute(request.licensePlate(),
                    toInstant(request.entryTime()));
            case "PARKED" -> handleParkedUseCase.execute(request.licensePlate(), request.lat(),
                    request.lng(), Instant.now());
            case "EXIT" -> handleExitUseCase.execute(request.licensePlate(),
                    toInstant(request.exitTime()));
            default -> throw new IllegalArgumentException(
                    "Unsupported event type: " + request.eventType());
        }
        return ResponseEntity.ok().build();
    }

    private Instant toInstant(java.time.LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.toInstant(ZoneOffset.UTC);
    }
}
