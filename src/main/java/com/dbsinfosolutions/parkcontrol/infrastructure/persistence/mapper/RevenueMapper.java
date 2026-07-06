package com.dbsinfosolutions.parkcontrol.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.dbsinfosolutions.parkcontrol.domain.model.Revenue;
import com.dbsinfosolutions.parkcontrol.infrastructure.persistence.entity.RevenueEntity;

@Component
public class RevenueMapper {

    public RevenueEntity toEntity(Revenue revenue) {
        RevenueEntity entity = new RevenueEntity();
        entity.setId(revenue.getId());
        entity.setDate(revenue.getDate());
        entity.setSector(revenue.getSector());
        entity.setAmount(revenue.getAmount());
        return entity;
    }

    public Revenue toDomain(RevenueEntity entity) {
        return new Revenue(entity.getId(), entity.getDate(), entity.getSector(),
                entity.getAmount());
    }
}
