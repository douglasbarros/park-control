package com.dbsinfosolutions.parkcontrol.infrastructure.client.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GarageCatalogResponse(@JsonProperty("garage") List<GarageItem> garage,
        @JsonProperty("spots") List<SpotItem> spots) {

    public record GarageItem(@JsonProperty("sector") String sector,
            @JsonProperty("base_price") BigDecimal basePrice,
            @JsonProperty("max_capacity") Integer maxCapacity,
            @JsonProperty("open_hour") String openHour,
            @JsonProperty("close_hour") String closeHour,
            @JsonProperty("duration_limit_minutes") Integer durationLimitMinutes) {
    }

    public record SpotItem(@JsonProperty("id") Long id, @JsonProperty("sector") String sector,
            @JsonProperty("lat") BigDecimal lat, @JsonProperty("lng") BigDecimal lng,
            @JsonProperty("occupied") Boolean occupied) {
    }
}
