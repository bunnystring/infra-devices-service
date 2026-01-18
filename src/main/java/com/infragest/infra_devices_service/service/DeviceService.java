package com.infragest.infra_devices_service.service;

import com.infragest.infra_devices_service.entity.Device;
import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import com.infragest.infra_devices_service.model.CreateDeviceRq;
import com.infragest.infra_devices_service.model.DeviceRs;
import com.infragest.infra_devices_service.model.DevicesBatchRq;

import java.util.List;
import java.util.Map;
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

    /**
     * Recupera información de múltiples devices por sus UUIDs y devuelve una
     * representación dinámica (List of Map) apropiada para consumo por otros microservicios
     * que esperan JSON libre (por ejemplo, un cliente Feign que mapea la respuesta a Map).
     *
     * @param ids lista de UUID a recuperar; si es {@code null} o vacía devuelve lista vacía
     * @return lista de mapas con campos relevantes por device (id, barcode, brand, name, status, createdAt, updatedAt)
     */
    List<Map<String, Object>> getDevicesByIds(List<UUID> ids);

    /**
     * Actualiza los estados de una lista de dispositivos.
     *
     * @param deviceIds Lista de IDs de los dispositivos a actualizar.
     * @param state El nuevo estado que se aplicará a los dispositivos.
     * @throws DeviceNotFoundException Si alguno de los dispositivos no existe en la base de datos.
     * @throws DeviceException Sí ocurre algún otro problema durante la actualización.
     */
    void updateDeviceStates(List<UUID> deviceIds, DeviceStatusEnum state);
}
