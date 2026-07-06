package com.dbsinfosolutions.parkcontrol.application.usecase;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSession;
import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSessionStatus;
import com.dbsinfosolutions.parkcontrol.domain.repository.ParkingSessionRepository;
import com.dbsinfosolutions.parkcontrol.domain.repository.SpotRepository;

@ExtendWith(MockitoExtension.class)
class HandleParkedUseCaseTest {

    @Mock
    private ParkingSessionRepository parkingSessionRepository;

    @Mock
    private SpotRepository spotRepository;

    @InjectMocks
    private HandleParkedUseCase handleParkedUseCase;

    @Test
    void ignoresDuplicateParkedEvent() {
        ParkingSession session = new ParkingSession(1L, "ABC1234", 10L, "A",
                Instant.parse("2025-01-01T12:00:00Z"), Instant.parse("2025-01-01T12:10:00Z"), null,
                ParkingSessionStatus.PARKED, BigDecimal.ZERO);

        when(parkingSessionRepository.findEntryByPlate("ABC1234")).thenReturn(Optional.of(session));

        handleParkedUseCase.execute("ABC1234", new BigDecimal("-23.1"), new BigDecimal("-46.1"),
                Instant.parse("2025-01-01T12:20:00Z"));

        verify(parkingSessionRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
