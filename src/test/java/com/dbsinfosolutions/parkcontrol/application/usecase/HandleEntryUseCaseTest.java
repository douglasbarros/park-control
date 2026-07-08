package com.dbsinfosolutions.parkcontrol.application.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.dbsinfosolutions.parkcontrol.domain.exception.GarageFullException;
import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSession;
import com.dbsinfosolutions.parkcontrol.domain.model.Spot;
import com.dbsinfosolutions.parkcontrol.domain.repository.GarageRepository;
import com.dbsinfosolutions.parkcontrol.domain.repository.ParkingSessionRepository;
import com.dbsinfosolutions.parkcontrol.domain.repository.SpotRepository;

@ExtendWith(MockitoExtension.class)
class HandleEntryUseCaseTest {

        @Mock
        private SpotRepository spotRepository;

        @Mock
        private ParkingSessionRepository parkingSessionRepository;

        @Mock
        private GarageRepository garageRepository;

        @InjectMocks
        private HandleEntryUseCase handleEntryUseCase;

        @Test
        void createsSessionWhenThereIsFreeSpot() {
                Spot spot = new Spot(1L, "A", new BigDecimal("-23.1"), new BigDecimal("-46.1"),
                                com.dbsinfosolutions.parkcontrol.domain.model.SpotStatus.FREE);
                when(parkingSessionRepository.findOpenByPlate("ABC1234"))
                                .thenReturn(Optional.empty());
                when(spotRepository.findFirstFreeForUpdate()).thenReturn(Optional.of(spot));
                when(spotRepository.save(spot)).thenReturn(spot);
                when(parkingSessionRepository.save(
                                org.mockito.ArgumentMatchers.any(ParkingSession.class))).thenAnswer(
                                                invocation -> invocation.getArgument(0));

                handleEntryUseCase.execute("ABC1234", Instant.parse("2025-01-01T12:00:00Z"));

                verify(spotRepository).save(spot);
                verify(parkingSessionRepository)
                                .save(org.mockito.ArgumentMatchers.any(ParkingSession.class));
        }

        @Test
        void throwsWhenGarageIsFull() {
                when(parkingSessionRepository.findOpenByPlate("ABC1234"))
                                .thenReturn(Optional.empty());
                when(spotRepository.findFirstFreeForUpdate()).thenReturn(Optional.empty());

                assertThrows(GarageFullException.class,
                                () -> handleEntryUseCase.execute("ABC1234", Instant.now()));
                verify(parkingSessionRepository, never()).save(org.mockito.ArgumentMatchers.any());
        }
}
