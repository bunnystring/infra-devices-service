package com.infragest.infra_devices_service.exception;

public class DeviceException extends RuntimeException {

    public enum Type {
        NOT_FOUND,
        BAD_REQUEST
    }

    private final Type type;

    public DeviceException(String message, Type type) {
        super(message);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
