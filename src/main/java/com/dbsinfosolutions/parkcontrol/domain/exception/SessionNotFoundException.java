package com.dbsinfosolutions.parkcontrol.domain.exception;

public class SessionNotFoundException extends BusinessException {

    public SessionNotFoundException(String plate) {
        super("No open session found for plate " + plate);
    }
}
