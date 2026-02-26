package com.infragest.infra_devices_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO para representar el estado de asignación activa de un dispositivo.
 *
 * @author bunnystring
 * @since 2026-02-27
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceAssignmentActiveRs {

    /**
     * Identificador único del dispositivo a consultar.
     */
    private UUID deviceId;

    /**
     * Indicador de asignación activa.
     * {@code true} si el dispositivo tiene al menos una asignación activa
     * (no liberada), {@code false} en caso contrario o si el dispositivo no existe.
     */
    private boolean active;
}
