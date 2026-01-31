package com.infragest.infra_devices_service.model;

import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO que representa una asignación de un dispositivo a una orden.
 * Este objeto se utiliza para transferir información simplificada
 * entre servicios y la API REST.
 *
 * @author bunnystring
 * @since 2025-11-06
 * @version 1.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceAssignmentDto {

    /**
     * Identificador único del dispositivo asociado a la asignación.
     */
    private UUID deviceId;

    /**
     * Identificador único de la orden relacionada con la asignación.
     */
    private UUID orderId;

    /**
     * Nombre del dispositivo asignado.
     */
    private String deviceName;

    /**
     * Estado actual del dispositivo en contexto de su asignación.
     */
    private DeviceStatusEnum deviceStatus;

    /**
     * Fecha y hora en la que el dispositivo fue asignado a una orden.
     */
    private LocalDateTime assignedAt;

    /**
     * Fecha y hora en la que el dispositivo fue liberado de la orden.
     * Si el dispositivo sigue asignado, este campo será {@code null}.
     */
    private LocalDateTime releasedAt;

}
