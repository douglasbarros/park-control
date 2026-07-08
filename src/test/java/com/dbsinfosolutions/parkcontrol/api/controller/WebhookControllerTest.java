package com.dbsinfosolutions.parkcontrol.api.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dbsinfosolutions.parkcontrol.api.exception.ApiExceptionHandler;
import com.dbsinfosolutions.parkcontrol.application.usecase.HandleEntryUseCase;
import com.dbsinfosolutions.parkcontrol.application.usecase.HandleExitUseCase;
import com.dbsinfosolutions.parkcontrol.application.usecase.HandleParkedUseCase;

@ExtendWith(MockitoExtension.class)
class WebhookControllerTest {

  @Mock
  private HandleEntryUseCase handleEntryUseCase;

  @Mock
  private HandleParkedUseCase handleParkedUseCase;

  @Mock
  private HandleExitUseCase handleExitUseCase;

  @InjectMocks
  private WebhookController webhookController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(webhookController)
        .setControllerAdvice(new ApiExceptionHandler()).build();
  }

  @Test
  void acceptsEntryEventWithLocalDateTimeFormat() throws Exception {
    mockMvc.perform(post("/webhook").contentType(MediaType.APPLICATION_JSON).content("""
        {
          "license_plate": "ABC1234",
          "entry_time": "2026-07-04T22:22:27",
          "event_type": "ENTRY"
        }
        """)).andExpect(status().isOk());

    verify(handleEntryUseCase).execute("ABC1234",
        LocalDateTime.of(2026, 7, 4, 22, 22, 27).toInstant(java.time.ZoneOffset.UTC));
    verifyNoInteractions(handleParkedUseCase, handleExitUseCase);
  }

  @Test
  void returnsBadRequestForUnsupportedEventType() throws Exception {
    mockMvc.perform(post("/webhook").contentType(MediaType.APPLICATION_JSON).content("""
        {
          "license_plate": "ABC1234",
          "entry_time": "2026-07-04T22:22:27",
          "event_type": "UNKNOWN"
        }
        """)).andExpect(status().isBadRequest());

    verifyNoInteractions(handleEntryUseCase, handleParkedUseCase, handleExitUseCase);
  }

  @Test
  void returnsUnprocessableContentWhenRequiredFieldIsMissing() throws Exception {
    mockMvc.perform(post("/webhook").contentType(MediaType.APPLICATION_JSON).content("""
        {
          "entry_time": "2026-07-04T22:22:27",
          "event_type": "ENTRY"
        }
        """)).andExpect(status().isUnprocessableContent());

    verifyNoInteractions(handleEntryUseCase, handleParkedUseCase, handleExitUseCase);
  }
}
