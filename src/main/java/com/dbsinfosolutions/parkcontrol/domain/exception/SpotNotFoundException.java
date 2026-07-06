package com.dbsinfosolutions.parkcontrol.domain.exception;

public class SpotNotFoundException extends BusinessException {

    public SpotNotFoundException() {
        super("Spot not found");
    }
}
