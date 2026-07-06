package com.dbsinfosolutions.parkcontrol.api.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbsinfosolutions.parkcontrol.domain.exception.GarageFullException;
import com.dbsinfosolutions.parkcontrol.domain.exception.SpotNotFoundException;

class ApiExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ErrorController())
                .setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    void mapsConflictToHttp409() throws Exception {
        mockMvc.perform(get("/errors/conflict")).andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"));
    }

    @Test
    void mapsNotFoundToHttp404() throws Exception {
        mockMvc.perform(get("/errors/not-found")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void mapsIllegalArgumentToHttp400() throws Exception {
        mockMvc.perform(get("/errors/bad-request")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @RestController
    static class ErrorController {

        @GetMapping("/errors/conflict")
        void conflict() {
            throw new GarageFullException();
        }

        @GetMapping("/errors/not-found")
        void notFound() {
            throw new SpotNotFoundException();
        }

        @GetMapping("/errors/bad-request")
        void badRequest() {
            throw new IllegalArgumentException("Unsupported event type");
        }
    }
}
