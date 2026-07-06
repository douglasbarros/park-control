package com.dbsinfosolutions.parkcontrol.infrastructure.persistence.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dbsinfosolutions.parkcontrol.domain.model.Garage;
import com.dbsinfosolutions.parkcontrol.infrastructure.persistence.entity.GarageEntity;

@Component
public class GarageMapper {

    public GarageEntity toEntity(Garage garage) {
        GarageEntity entity = new GarageEntity();
        entity.setId(garage.getId());
        entity.setSector(garage.getSector());
        entity.setBasePrice(garage.getBasePrice());
        entity.setCapacity(garage.getCapacity());
        entity.setOpenHour(garage.getOpenHour());
        entity.setCloseHour(garage.getCloseHour());
        entity.setDurationLimitMinutes(garage.getDurationLimitMinutes());
        return entity;
    }

    public Garage toDomain(GarageEntity entity) {
        Garage garage = new Garage(entity.getId(), entity.getSector(), entity.getBasePrice(),
                entity.getCapacity(), entity.getOpenHour(), entity.getCloseHour(),
                entity.getDurationLimitMinutes());
        return garage;
    }

    public List<Garage> toDomainList(List<GarageEntity> entities) {
        return entities.stream().map(this::toDomain).toList();
    }
}
