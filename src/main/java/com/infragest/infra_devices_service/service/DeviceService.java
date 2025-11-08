package com.infragest.infra_devices_service.service;

import com.infragest.infra_devices_service.entity.Device;
import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import com.infragest.infra_devices_service.model.CreateDeviceRq;
import com.infragest.infra_devices_service.model.DeviceRs;

import java.util.List;
import java.util.UUID;

/**
 * Servicio para operaciones CRUD y consultas sobre dispositivos.
 *
 * @author bunnystring
 * @since 2025-11-07
 */
public interface DeviceService {

    /**
     * Crea un nuevo dispositivo a partir del request y lo devuelve como DTO.
     *
     * @param device datos para crear el dispositivo
     * @return DeviceRs creado
     */
    DeviceRs saveDevice(CreateDeviceRq device);

    /**
     * Obtiene un dispositivo por su id.
     *
     * @param id identificador UUID
     * @return DeviceRs correspondiente
     */
    DeviceRs getDeviceById(UUID id);

    /**
     * Obtiene la entidad Device por su barcode.
     *
     * @param barcode código de barras
     * @return entidad Device (puede ser null)
     */
    Device getDeviceByBarcode(String barcode);

    /**
     * Devuelve todos los dispositivos como DTO.
     *
     * @return lista de DeviceRs (puede ser vacía)
     */
    List<DeviceRs> getAllDevices();

    /**
     * Devuelve los dispositivos con el estado indicado como DTO.
     *
     * @param status estado por el que filtrar
     * @return lista de DeviceRs (puede ser vacía)
     */
    List<DeviceRs> getDevicesByStatus(DeviceStatusEnum status);

    /**
     * Devuelve los dispositivos cuyos estados están en la lista indicada.
     *
     * @param statuses lista de estados para filtrar
     * @return lista de DeviceRs (puede ser vacía)
     */
    List<DeviceRs> getDevicesByStatuses(List<DeviceStatusEnum> statuses);

    /**
     * Elimina un dispositivo por su id.
     *
     * @param id identificador UUID del dispositivo a eliminar
     */
    void deleteDevice(UUID id);

    /**
     * Actualiza un dispositivo existente y devuelve el DTO actualizado.
     *
     * @param id identificador UUID del dispositivo
     * @param request datos para actualizar el dispositivo
     * @return DeviceRs actualizado
     */
    DeviceRs updateDevice(UUID id, CreateDeviceRq request);
}
