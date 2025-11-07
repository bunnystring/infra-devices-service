package com.infragest.infra_devices_service.exception;

/**
 * Excepción de negocio para el módulo Device (unchecked para rollback automático).
 */
public class DeviceException extends RuntimeException {

    public enum Type {
        NOT_FOUND,
        BAD_REQUEST,
        INTERNAL_SERVER
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
