package com.infragest.infra_devices_service.enums;

/**
 * Estados posibles de un {@code Device}.
 *
 * Valores simples que describen el estado físico o de uso del dispositivo.
 *
 * @author bunnystring
 * @since 2025-11-07
 */
public enum DeviceStatusEnum {
    GOOD_CONDITION, // En buen estado y disponible para uso.
    FAIR, // Estado aceptable, pero con desgaste leve.
    OCCUPIED, // Actualmente, en uso / asignado.
    NEEDS_REPAIR // Requiere reparación.
}
