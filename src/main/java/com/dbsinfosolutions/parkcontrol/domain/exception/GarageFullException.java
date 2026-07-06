package com.dbsinfosolutions.parkcontrol.domain.exception;

public class GarageFullException extends BusinessException {

    public GarageFullException() {
        super("Garage is full");
    }
}
