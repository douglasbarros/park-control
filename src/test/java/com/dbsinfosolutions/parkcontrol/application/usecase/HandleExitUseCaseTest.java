package com.dbsinfosolutions.parkcontrol.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dbsinfosolutions.parkcontrol.domain.model.Garage;
import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSession;
import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSessionStatus;
import com.dbsinfosolutions.parkcontrol.domain.model.Revenue;
import com.dbsinfosolutions.parkcontrol.domain.model.Spot;
import com.dbsinfosolutions.parkcontrol.domain.model.SpotStatus;
import com.dbsinfosolutions.parkcontrol.domain.repository.GarageRepository;
import com.dbsinfosolutions.parkcontrol.domain.repository.ParkingSessionRepository;
import com.dbsinfosolutions.parkcontrol.domain.repository.RevenueRepository;
import com.dbsinfosolutions.parkcontrol.domain.repository.SpotRepository;
import com.dbsinfosolutions.parkcontrol.domain.service.PricingService;

@ExtendWith(MockitoExtension.class)
class HandleExitUseCaseTest {

    @Mock
    private ParkingSessionRepository parkingSessionRepository;

    @Mock
    private SpotRepository spotRepository;

    @Mock
    private RevenueRepository revenueRepository;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private HandleExitUseCase handleExitUseCase;

    @Test
    void releasesSpotAndGeneratesRevenue() {
        ParkingSession session =
                new ParkingSession(1L, "ABC1234", 10L, "A", Instant.parse("2025-01-01T12:00:00Z"),
                        null, null, ParkingSessionStatus.PARKED, BigDecimal.ZERO);
        Spot spot = new Spot(10L, "A", new BigDecimal("-23.1"), new BigDecimal("-46.1"),
                SpotStatus.OCCUPIED);
        Garage garage = new Garage(1L, "A", new BigDecimal("10.00"), 10, null, null, null);
        when(parkingSessionRepository.findOpenByPlate("ABC1234")).thenReturn(Optional.of(session));
        when(spotRepository.findById(10L)).thenReturn(Optional.of(spot));
        when(garageRepository.findBySector("A")).thenReturn(Optional.of(garage));
        when(spotRepository.countByStatus(SpotStatus.OCCUPIED)).thenReturn(8L);
        when(pricingService.calculateAmount(org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.eq(new BigDecimal("10.00")),
                org.mockito.ArgumentMatchers.anyDouble())).thenReturn(new BigDecimal("12.50"));
        when(revenueRepository.findByDateAndSector(org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.eq("A"))).thenReturn(Optional.empty());
        when(revenueRepository.save(org.mockito.ArgumentMatchers.any(Revenue.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(parkingSessionRepository.save(org.mockito.ArgumentMatchers.any(ParkingSession.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(spotRepository.save(org.mockito.ArgumentMatchers.any(Spot.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        handleExitUseCase.execute("ABC1234", Instant.parse("2025-01-01T13:00:00Z"));

        ArgumentCaptor<Spot> spotCaptor = ArgumentCaptor.forClass(Spot.class);
        verify(spotRepository).save(spotCaptor.capture());
        assertEquals(SpotStatus.FREE, spotCaptor.getValue().getStatus());
        ArgumentCaptor<Revenue> revenueCaptor = ArgumentCaptor.forClass(Revenue.class);
        verify(revenueRepository).save(revenueCaptor.capture());
        assertEquals(new BigDecimal("12.50"), revenueCaptor.getValue().getAmount());
    }
}
