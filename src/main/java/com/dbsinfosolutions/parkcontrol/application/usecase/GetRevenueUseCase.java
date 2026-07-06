package com.dbsinfosolutions.parkcontrol.application.usecase;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.dbsinfosolutions.parkcontrol.application.dto.RevenueResponse;
import com.dbsinfosolutions.parkcontrol.domain.repository.RevenueRepository;

@Service
public class GetRevenueUseCase {

    private final RevenueRepository revenueRepository;

    public GetRevenueUseCase(RevenueRepository revenueRepository) {
        this.revenueRepository = revenueRepository;
    }

    public RevenueResponse execute(LocalDate date, String sector) {
        BigDecimal amount = revenueRepository.findByDateAndSector(date, sector)
                .map(revenue -> revenue.getAmount()).orElse(BigDecimal.ZERO);
        return new RevenueResponse(amount, "BRL", Instant.now(), sector);
    }
}
