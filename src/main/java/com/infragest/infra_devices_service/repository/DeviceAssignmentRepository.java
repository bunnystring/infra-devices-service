package com.infragest.infra_devices_service.repository;

import com.infragest.infra_devices_service.entity.DeviceAssignment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceAssignmentRepository extends JpaRepository<DeviceAssignment, UUID> {

    /**
     * Busca la asignación activa (released_at IS NULL) de un dispositivo.
     *
     * @param deviceId ID del dispositivo.
     * @return Optional con la asignación activa, si existe; vacío en caso contrario.
     */
    Optional<DeviceAssignment> findByDeviceIdAndReleasedAtIsNull(UUID deviceId);

    /**
     * Busca la asignación activa de un dispositivo para una orden específica.
     *
     * @param orderId  ID de la orden.
     * @param deviceId ID del dispositivo.
     * @return Optional con la asignación activa para el dispositivo en la orden indicada, si existe.
     */
    Optional<DeviceAssignment> findByOrderIdAndDeviceIdAndReleasedAtIsNull(UUID orderId, UUID deviceId);

    /**
     * Busca y bloquea la asignación activa de un dispositivo para evitar conflictos concurrentes.
     *
     * @param deviceId ID del dispositivo.
     * @return Optional con la asignación activa bloqueada.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DeviceAssignment> findWithLockByDeviceIdAndReleasedAtIsNull(UUID deviceId);

    /**
     * Obtiene todas las asignaciones históricas de un dispositivo.
     *
     * @param deviceId ID del dispositivo a consultar.
     * @return Lista de entidades {@link DeviceAssignment} con asignaciones.
     */
    List<DeviceAssignment> findAllByDeviceIdOrderByAssignedAtDesc(UUID deviceId);
}
