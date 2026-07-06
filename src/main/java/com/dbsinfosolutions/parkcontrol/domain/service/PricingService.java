package com.dbsinfosolutions.parkcontrol.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

import org.springframework.stereotype.Service;

@Service
public class PricingService {

    public BigDecimal calculateAmount(Duration duration, BigDecimal basePrice,
            double occupancyRate) {
        long totalSeconds = duration.getSeconds();
        if (totalSeconds <= Duration.ofMinutes(30).getSeconds()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        long billableHours = (long) Math.ceil(totalSeconds / 3600.0);
        BigDecimal rawAmount = basePrice.multiply(BigDecimal.valueOf(billableHours));
        return applyOccupancyAdjustment(rawAmount, occupancyRate).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal applyOccupancyAdjustment(BigDecimal amount, double occupancyRate) {
        if (occupancyRate < 0.25d) {
            return amount.multiply(BigDecimal.valueOf(0.90d));
        }
        if (occupancyRate <= 0.50d) {
            return amount;
        }
        if (occupancyRate <= 0.75d) {
            return amount.multiply(BigDecimal.valueOf(1.10d));
        }
        return amount.multiply(BigDecimal.valueOf(1.25d));
    }
}
