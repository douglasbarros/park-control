package com.dbsinfosolutions.parkcontrol.domain.model;

import java.math.BigDecimal;
import java.util.List;

public record GarageCatalog(String sector, BigDecimal basePrice, Integer capacity, String openHour,
        String closeHour, Integer durationLimitMinutes, List<SpotCatalog> spots) {
}
