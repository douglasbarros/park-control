package com.dbsinfosolutions.parkcontrol.api.controller;

import java.time.LocalDate;
import java.time.ZoneOffset;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dbsinfosolutions.parkcontrol.application.dto.RevenueResponse;
import com.dbsinfosolutions.parkcontrol.application.usecase.GetRevenueUseCase;

@RestController
@RequestMapping("/revenue")
public class RevenueController {

    private final GetRevenueUseCase getRevenueUseCase;

    public RevenueController(GetRevenueUseCase getRevenueUseCase) {
        this.getRevenueUseCase = getRevenueUseCase;
    }

    @GetMapping
    public ResponseEntity<RevenueResponse> getRevenue(
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate date,
            @RequestParam String sector) {
        LocalDate effectiveDate = date == null ? LocalDate.now(ZoneOffset.UTC) : date;
        return ResponseEntity.ok(getRevenueUseCase.execute(effectiveDate, sector));
    }
}
