package com.dbsinfosolutions.parkcontrol.infrastructure.persistence.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dbsinfosolutions.parkcontrol.domain.model.Spot;
import com.dbsinfosolutions.parkcontrol.infrastructure.persistence.entity.SpotEntity;

@Component
public class SpotMapper {

    public SpotEntity toEntity(Spot spot) {
        SpotEntity entity = new SpotEntity();
        entity.setId(spot.getId());
        entity.setSector(spot.getSector());
        entity.setLatitude(spot.getLatitude());
        entity.setLongitude(spot.getLongitude());
        entity.setStatus(spot.getStatus());
        return entity;
    }

    public Spot toDomain(SpotEntity entity) {
        Spot spot = new Spot(entity.getId(), entity.getSector(), entity.getLatitude(),
                entity.getLongitude(), entity.getStatus());
        return spot;
    }

    public List<Spot> toDomainList(List<SpotEntity> entities) {
        return entities.stream().map(this::toDomain).toList();
    }
}
