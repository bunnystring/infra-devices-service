package com.infragest.infra_devices_service.service;

import com.infragest.infra_devices_service.entity.DeviceAssignment;
import com.infragest.infra_devices_service.enums.DeviceStatusEnum;

import java.util.List;
import java.util.UUID;

/**
 * Servicio para gestionar las asignaciones de dispositivos con órdenes.
 *
 * Proporciona métodos para asignar dispositivos a órdenes, liberar asignaciones activas y consultar
 * el historial de asignaciones relacionadas con un dispositivo.
 *
 * Encapsula la lógica relacionada con la entidad {@link DeviceAssignment}.
 *
 * @author bunnystring
 * @since 2026-01-30
 */
public interface DeviceAssignmentService {

    /**
     * Asigna un dispositivo a una orden si está disponible.
     *
     * @param orderId  Identificador único de la orden.
     * @param deviceId Identificador único del dispositivo.
     */
    void assignDeviceToOrder(UUID orderId, UUID deviceId);

    /**
     * Libera un dispositivo asociado a una orden específica, finalizando la asignación activa.
     *
     * @param deviceId Identificador único del dispositivo.
     */
    void releaseDeviceFromOrder(UUID deviceId, DeviceStatusEnum status);

    /**
     * Obtiene todas las asignaciones históricas (finalizadas) de un dispositivo.
     *
     * @param deviceId Identificador único del dispositivo.
     * @return Lista de entidades {@link DeviceAssignment} que representan el historial de asignaciones para el dispositivo.
     */
    List<DeviceAssignment> getDeviceAssignmentHistory(UUID deviceId);

    /**
     * Verifica si un dispositivo tiene una asignación activa en este momento.
     *
     * @param deviceId Identificador único del dispositivo.
     * @return {@code true} si el dispositivo tiene una asignación activa (releasedAt es null), {@code false} en caso contrario.
     */
    boolean hasActiveAssignment(UUID deviceId);
}
