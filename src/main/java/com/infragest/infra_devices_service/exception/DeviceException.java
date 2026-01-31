package com.infragest.infra_devices_service.exception;

/**
 * Excepción de negocio específica del módulo Device.
 *
 * Es una excepción unchecked (extiende {@link RuntimeException}) para permitir el rollback
 * automático en transacciones gestionadas por Spring cuando se lanza desde la capa de servicio.
 *
 * @author bunnystring
 * @since 2025-11-07
 */
public class DeviceException extends RuntimeException {

    /**
     * Tipos de error de negocio usados por {@link DeviceException}.
     */
    public enum Type {
        NOT_FOUND,
        BAD_REQUEST,
        INTERNAL_SERVER,
        CONFLICT
    }

    /**
     * Tipo de la excepción (clasificación del error).
     */
    private final Type type;

    /**
     * Crea una nueva {@code DeviceException} con mensaje y tipo.
     *
     * @param message mensaje descriptivo del error
     * @param type    tipo de excepción (clasificación)
     */
    public DeviceException(String message, Type type) {
        super(message);
        this.type = type;
    }

    /**
     * Obtiene el tipo (clasificación) de la excepción.
     *
     * @return el {@link Type} asociado a esta excepción
     */
    public Type getType() {
        return type;
    }
}
