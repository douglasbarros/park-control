package com.dbsinfosolutions.parkcontrol.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.dbsinfosolutions.parkcontrol.domain.model.ParkingSession;
import com.dbsinfosolutions.parkcontrol.infrastructure.persistence.entity.ParkingSessionEntity;

@Component
public class ParkingSessionMapper {

    public ParkingSessionEntity toEntity(ParkingSession session) {
        ParkingSessionEntity entity = new ParkingSessionEntity();
        entity.setId(session.getId());
        entity.setPlate(session.getPlate());
        entity.setSpotId(session.getSpotId());
        entity.setSector(session.getSector());
        entity.setEntryTime(session.getEntryTime());
        entity.setParkedTime(session.getParkedTime());
        entity.setExitTime(session.getExitTime());
        entity.setStatus(session.getStatus());
        entity.setAmountPaid(session.getAmountPaid());
        return entity;
    }

    public ParkingSession toDomain(ParkingSessionEntity entity) {
        return new ParkingSession(entity.getId(), entity.getPlate(), entity.getSpotId(),
                entity.getSector(), entity.getEntryTime(), entity.getParkedTime(),
                entity.getExitTime(), entity.getStatus(), entity.getAmountPaid());
    }
}
