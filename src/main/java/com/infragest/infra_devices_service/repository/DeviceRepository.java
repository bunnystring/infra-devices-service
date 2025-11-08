package com.infragest.infra_devices_service.repository;

import com.infragest.infra_devices_service.entity.Device;
import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad {@link Device}.
 *
 * Proporciona métodos para buscar dispositivos por barcode y por estado(s).
 *
 * @author bunnystring
 * @since 2025-11-07
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

    /**
     * Busca un dispositivo por su código de barras (único).
     *
     * @param barcode código de barras del dispositivo
     * @return Optional con el {@link Device} si existe, vacío en caso contrario
     */
    Optional<Device> findByBarcode(String barcode);

    /**
     * Obtiene todos los dispositivos con el estado indicado.
     *
     * @param status estado por el que filtrar
     * @return lista de {@link Device} que cumplen el estado (puede ser vacía)
     */
    List<Device> findAllByStatus(DeviceStatusEnum status);

    /**
     * Obtiene todos los dispositivos cuyos estados están contenidos en la lista indicada.
     *
     * @param statuses lista de {@link DeviceStatusEnum} para filtrar
     * @return lista de {@link Device} que cumplen con alguno de los estados (puede ser vacía)
     */
    List<Device> findAllByStatusIn(List<DeviceStatusEnum> statuses);
}
