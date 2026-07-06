package com.dbsinfosolutions.parkcontrol.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Duration;

import org.junit.jupiter.api.Test;

class PricingServiceTest {

    private final PricingService pricingService = new PricingService();

    @Test
    void returnsZeroForThirtyMinutes() {
        assertEquals(new BigDecimal("0.00"), pricingService.calculateAmount(Duration.ofMinutes(30),
                new BigDecimal("10.00"), 0.50d));
    }

    @Test
    void chargesOneHourAfterThirtyMinutesAndOneSecond() {
        assertEquals(new BigDecimal("10.00"), pricingService.calculateAmount(
                Duration.ofMinutes(30).plusSeconds(1), new BigDecimal("10.00"), 0.50d));
    }

    @Test
    void roundsUpAfterThirtyOneMinutes() {
        assertEquals(new BigDecimal("10.00"), pricingService.calculateAmount(Duration.ofMinutes(31),
                new BigDecimal("10.00"), 0.50d));
    }

    @Test
    void roundsUpAfterSixtyOneMinutes() {
        assertEquals(new BigDecimal("20.00"), pricingService.calculateAmount(Duration.ofMinutes(61),
                new BigDecimal("10.00"), 0.50d));
    }

    @Test
    void appliesTwentyFivePercentIncreaseAboveSeventyFivePercentOccupancy() {
        assertEquals(new BigDecimal("25.00"), pricingService.calculateAmount(Duration.ofMinutes(61),
                new BigDecimal("10.00"), 0.80d));
    }
}
