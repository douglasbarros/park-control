package com.dbsinfosolutions.parkcontrol.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;

public record WebhookEventRequest(@JsonProperty("license_plate") @NotBlank String licensePlate,
        @JsonProperty("entry_time") @JsonFormat(
                pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime entryTime,
        @JsonProperty("exit_time") @JsonFormat(
                pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime exitTime,
        @JsonProperty("lat") BigDecimal lat, @JsonProperty("lng") BigDecimal lng,
        @JsonProperty("event_type") @NotBlank String eventType) {
}
