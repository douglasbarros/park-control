package com.dbsinfosolutions.parkcontrol.domain.model;

import java.math.BigDecimal;

public record SpotCatalog(Long id, String sector, BigDecimal latitude, BigDecimal longitude,
        Boolean occupied) {
}
