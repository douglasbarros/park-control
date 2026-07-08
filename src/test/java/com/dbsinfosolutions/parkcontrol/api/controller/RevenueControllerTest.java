package com.dbsinfosolutions.parkcontrol.api.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.dbsinfosolutions.parkcontrol.api.exception.ApiExceptionHandler;
import com.dbsinfosolutions.parkcontrol.application.dto.RevenueResponse;
import com.dbsinfosolutions.parkcontrol.application.usecase.GetRevenueUseCase;

@ExtendWith(MockitoExtension.class)
class RevenueControllerTest {

        @Mock
        private GetRevenueUseCase getRevenueUseCase;

        @InjectMocks
        private RevenueController revenueController;

        private MockMvc mockMvc;

        @BeforeEach
        void setUp() {
                mockMvc = MockMvcBuilders.standaloneSetup(revenueController)
                                .setControllerAdvice(new ApiExceptionHandler()).build();
        }

        @Test
        void returnsRevenueForProvidedDate() throws Exception {
                when(getRevenueUseCase.execute(eq(LocalDate.of(2026, 7, 4)), eq("A")))
                                .thenReturn(new RevenueResponse(new BigDecimal("12.50"), "BRL",
                                                Instant.parse("2026-07-04T12:00:00Z"), "A"));

                mockMvc.perform(get("/revenue").param("date", "2026-07-04").param("sector", "A"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.amount").value(12.50))
                                .andExpect(jsonPath("$.currency").value("BRL"))
                                .andExpect(jsonPath("$.sector").value("A"));

                verify(getRevenueUseCase).execute(LocalDate.of(2026, 7, 4), "A");
        }

        @Test
        void defaultsDateToTodayWhenMissing() throws Exception {
                LocalDate today = LocalDate.now(ZoneOffset.UTC);
                when(getRevenueUseCase.execute(eq(today), eq("A")))
                                .thenReturn(new RevenueResponse(BigDecimal.ZERO, "BRL",
                                                Instant.parse("2026-07-04T12:00:00Z"), "A"));

                mockMvc.perform(get("/revenue").param("sector", "A")).andExpect(status().isOk())
                                .andExpect(jsonPath("$.amount").value(0.0))
                                .andExpect(jsonPath("$.currency").value("BRL"))
                                .andExpect(jsonPath("$.sector").value("A"));

                verify(getRevenueUseCase).execute(today, "A");
        }
}
