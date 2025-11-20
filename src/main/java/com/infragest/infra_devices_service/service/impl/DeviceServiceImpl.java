package com.infragest.infra_devices_service.service.impl;

import com.infragest.infra_devices_service.entity.Device;
import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import com.infragest.infra_devices_service.exception.DeviceException;
import com.infragest.infra_devices_service.model.CreateDeviceRq;
import com.infragest.infra_devices_service.model.DeviceRs;
import com.infragest.infra_devices_service.model.DevicesBatchRq;
import com.infragest.infra_devices_service.model.RestoreDevicesRq;
import com.infragest.infra_devices_service.repository.DeviceRepository;
import com.infragest.infra_devices_service.service.DeviceService;
import com.infragest.infra_devices_service.util.MessageException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio {@link DeviceService} que gestiona la lógica de negocio
 * para las operaciones CRUD sobre la entidad {@link Device}.
 *
 * Proporciona métodos para crear, consultar, listar por estado(s) y eliminar dispositivos.
 *
 * @author bunnystring
 * @since 2025-11-07
 */
@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    /**
     * Inyección de dependencia: Repositorio de dispositivos.
     */
    private final DeviceRepository deviceRepository;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param deviceRepository
     */
    public DeviceServiceImpl(
            DeviceRepository deviceRepository)
    {
        this.deviceRepository = deviceRepository;
    }

    /**
     * Crea y persiste un nuevo dispositivo.
     *
     * Válida que no exista ya un dispositivo con el mismo barcode; en caso contrario
     * lanza {@link DeviceException} con tipo {@code BAD_REQUEST}. Si ocurre un error
     * de integridad en la persistencia se lanza {@link DeviceException} con tipo
     * {@code INTERNAL_SERVER}.
     *
     * @param request datos del dispositivo a crear
     * @return DeviceRs con los datos persistidos
     * @throws DeviceException si ya existe un dispositivo con el mismo barcode o si ocurre un error interno
     *
     * @author bunnystring
     * @since 2025-11-07
     */
    @Transactional
    @Override
    public DeviceRs saveDevice(CreateDeviceRq request) {

        // Validar si ya existe un dispositivo con el mismo barcode, se lanza excepción de negocio.
        if (deviceRepository.findByBarcode(request.getBarcode()).isPresent()) {
            throw new DeviceException(
                    String.format(MessageException.DEVICE_ALREADY_EXISTS, request.getBarcode()),
                    DeviceException.Type.BAD_REQUEST
            );
        }

        // Construcción de la entidad Device a partir del DTO. Se hace trim de los campos de texto para normalizar la entrada.
        Device deviceEntity = new Device();
        deviceEntity.setName(request.getName() != null ? request.getName().trim() : null);
        deviceEntity.setBrand(request.getBrand() != null ? request.getBrand().trim() : null);
        deviceEntity.setBarcode(request.getBarcode() != null ? request.getBarcode().trim() : null);
        deviceEntity.setStatus(request.getStatus());

        try {

            // Persistir la entidad.
            Device saved = deviceRepository.save(deviceEntity);

            return buildDeviceRs(deviceEntity);

        } catch (org.springframework.dao.DataIntegrityViolationException ex){
            log.error("Error al crear Device", ex);
            throw new DeviceException("Ha Ocurrido un error interno en el servidor", DeviceException.Type.INTERNAL_SERVER);
        }
    }

    /**
     * Obtiene un dispositivo por su identificador UUID.
     *
     * @param id identificador UUID del dispositivo
     * @return {@link DeviceRs} correspondiente
     * @throws DeviceException si no se encuentra el dispositivo (tipo NOT_FOUND)
     */
    @Override
    public DeviceRs getDeviceById(UUID id) {

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceException(
                        String.format(MessageException.DEVICE_ALREADY_EXISTS, id),
                        DeviceException.Type.NOT_FOUND
                ));

        return buildDeviceRs(device);
    }

    /**
     * Obtiene la entidad {@link Device} por su código de barras.
     *
     * @param barcode código de barras del dispositivo
     * @return entidad {@link Device}
     * @throws DeviceException si no se encuentra el dispositivo (tipo NOT_FOUND)
     */
    @Override
    public Device getDeviceByBarcode(String barcode) {
        return deviceRepository.findByBarcode(barcode)
                .orElseThrow(() -> new DeviceException(
                        String.format(MessageException.DEVICE_NOT_FOUND_BY_BARCODE, barcode),
                        DeviceException.Type.NOT_FOUND
                ));
    }

    /**
     * Devuelve todos los dispositivos como DTO {@code DeviceRs}.
     *
     * @return lista de {@code DeviceRs} (puede ser vacía)
     *
     * @author bunnystring
     * @since 2025-11-07
     */
    @Override
    public List<DeviceRs> getAllDevices() {
        List<Device> devices = deviceRepository.findAll();
        return buildDeviceRsList(devices);
    }

    /**
     * Devuelve los dispositivos con el {@code status} indicado como DTOs {@code DeviceRs}.
     *
     * @param status estado por el que filtrar
     * @return lista de {@code DeviceRs} (puede ser vacía)
     *
     * @author bunnystring
     * @since 2025-11-07
     */
    @Override
    public List<DeviceRs> getDevicesByStatus(DeviceStatusEnum status) {
        List<Device> devices = deviceRepository.findAllByStatus(status);
        return buildDeviceRsList(devices);
    }

    /**
     * Devuelve los dispositivos cuyos estados están contenidos en la lista indicada.
     *
     * @param statuses lista de {@link DeviceStatusEnum} para filtrar
     * @return lista de {@link DeviceRs} (puede ser vacía)
     * @throws IllegalArgumentException si {@code statuses} es nula (se asume no nula en el flujo actual)
     */
    @Override
    public List<DeviceRs> getDevicesByStatuses(List<DeviceStatusEnum> statuses) {
        List<Device> devices = deviceRepository.findAllByStatusIn(statuses);
        return buildDeviceRsList(devices);
    }

    /**
     * Elimina un dispositivo por su id.
     *
     * @param id identificador UUID del dispositivo a eliminar
     * @throws DeviceException si no existe el dispositivo (tipo NOT_FOUND)
     */
    @Override
    public void deleteDevice(UUID id) {
        if (!deviceRepository.existsById(id)) {
            throw new DeviceException(
                    String.format(MessageException.DEVICE_NOT_FOUND_BY_ID, id),
                    DeviceException.Type.NOT_FOUND
            );
        }
        deviceRepository.deleteById(id);
    }

    /**
     * Construye el DTO DeviceRs a partir de la entidad Device usando el builder.
     *
     * @param device entidad a convertir (se asume no nula)
     * @return DeviceRs mapeado
     *
     * @author bunnystring
     * @since 2025-11-07
     */
    private DeviceRs buildDeviceRs(Device device) {
        return DeviceRs.builder()
                .id(device.getId())
                .name(device.getName())
                .brand(device.getBrand())
                .barcode(device.getBarcode())
                .status(device.getStatus())
                .createdAt(device.getCreatedAt() != null
                        ? device.getCreatedAt()
                        : null)
                .updatedAt(device.getUpdatedAt() != null
                        ? device.getUpdatedAt()
                        : null)
                .build();
    }

    /**
     * Construye una lista de {@code DeviceRs} a partir de una lista de entidades {@code Device}.
     *
     * @param devices lista de entidades Device (se asume no nula)
     * @return lista de DeviceRs mapeados (puede ser vacía)
     *
     * @author bunnystring
     * @since 2025-11-07
     */
    private List<DeviceRs> buildDeviceRsList(List<Device> devices) {
        return devices.stream()
                .map(this::buildDeviceRs)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Actualiza un dispositivo existente.
     *
     * - Lanza {@link DeviceException} tipo NOT_FOUND si no existe el dispositivo.
     * - Lanza {@link DeviceException} tipo BAD_REQUEST si el barcode ya está en uso por otro dispositivo.
     *
     * @param id identificador UUID del dispositivo
     * @param request datos para actualizar
     * @return DeviceRs actualizado
     */
    @Transactional
    @Override
    public DeviceRs updateDevice(UUID id, CreateDeviceRq request) {

        Device device = deviceRepository.findById(id)
                .orElseThrow( () -> new DeviceException(
                        String.format(MessageException.DEVICE_NOT_FOUND_BY_ID, id),
                        DeviceException.Type.NOT_FOUND
                ));

        // Si se intenta cambiar el barcode, asegurar unicidad (si otro registro lo tiene -> BAD_REQUEST)
        String newBarcode = request.getBarcode() != null ? request.getBarcode().trim() : null;
        if (newBarcode != null && !newBarcode.equals(device.getBarcode())) {
            deviceRepository.findByBarcode(newBarcode).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new DeviceException(
                            String.format(MessageException.DEVICE_BARCODE_ALREADY_EXISTS, newBarcode),
                            DeviceException.Type.BAD_REQUEST
                    );
                }
            });
            device.setBarcode(newBarcode);
        }

        // Actualizar otros campos (hacer trim/null checks)
        if (request.getName() != null) {
            device.setName(request.getName().trim());
        }
        if (request.getBrand() != null) {
            device.setBrand(request.getBrand().trim());
        }
        if (request.getStatus() != null) {
            device.setStatus(request.getStatus());
        }

        try {
            Device saved = deviceRepository.save(device);
            return buildDeviceRs(saved);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            log.error("Error al actualizar Device {}", id, ex);
            throw new DeviceException(MessageException.DEVICE_ERROR_SAVING, DeviceException.Type.INTERNAL_SERVER);
        }
    }

    /**
     * Recupera información de múltiples devices por sus UUIDs y devuelve una
     * representación dinámica (List of Map) apropiada para consumo por otros microservicios
     * que esperan JSON libre (por ejemplo, un cliente Feign que mapea a Map).
     *
     * @param ids lista de UUID a recuperar; si es {@code null} o vacía devuelve lista vacía
     * @return lista de mapas con campos relevantes por device (id, barcode, brand, name, status, createdAt, updatedAt)
     * @throws RuntimeException si ocurre un error de acceso a datos (envuelto)
     */
    @Override
    @Transactional()
    public List<Map<String, Object>>getDevicesByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        try {
            List<Device> devices = deviceRepository.findAllById(ids);
            return devices.stream().map(this::toMap).collect(Collectors.toList());
        } catch (DataAccessException dae) {
            log.error("Error reading devices by ids {}", ids, dae);
            throw new DeviceException(MessageException.DEVICE_NOT_FOUND_BY_ID, DeviceException.Type.INTERNAL_SERVER);
        }
    }

    /**
     * Reserva/actualiza el estado de los devices indicados.
     *
     * @param ids lista de UUIDs
     * @param state estado objetivo como {@link DeviceStatusEnum}
     * @return mapa con la respuesta de la operación
     */
    @Override
    @Transactional
    public Map<String, Object> reserveDevices(List<UUID> ids, DeviceStatusEnum state) {
        if (ids == null || ids.isEmpty()) {
            return Map.of("success", false, "message", "deviceIds empty");
        }

        try {
            List<Device> found = deviceRepository.findAllById(ids);
            Set<UUID> foundIds = found.stream().map(Device::getId).collect(Collectors.toSet());
            List<UUID> missing = ids.stream().filter(id -> !foundIds.contains(id)).collect(Collectors.toList());
            if (!missing.isEmpty()) {
                return Map.of("success", false, "message", "Devices not found: " + missing);
            }

            found.forEach(d -> d.setStatus(state));
            deviceRepository.saveAll(found);
            return Map.of("success", true);
        } catch (DataAccessException dae) {
            log.error("Error reservando devices {} -> {}", ids, dae.getMessage(), dae);
            return Map.of("success", false, "message", "DB error");
        }
    }

    /**
     * Restaura los estados originales de una lista de devices.
     *
     * @param items lista de {@link RestoreDevicesRq.RestoreItem}
     * @return mapa con la respuesta de la operación
     */
    @Override
    @Transactional
    public Map<String, Object> restoreDeviceStates(List<RestoreDevicesRq.RestoreItem> items) {
        if (items == null || items.isEmpty()) {
            return Map.of("success", false, "message", "items empty");
        }

        try {
            // extraer ids y mapear estado a aplicar por id
            List<UUID> ids = items.stream()
                    .map(RestoreDevicesRq.RestoreItem::getDeviceId)
                    .collect(Collectors.toList());

            List<Device> found = deviceRepository.findAllById(ids);
            Set<UUID> foundIds = found.stream().map(Device::getId).collect(Collectors.toSet());
            List<UUID> missing = ids.stream().filter(id -> !foundIds.contains(id)).collect(Collectors.toList());
            if (!missing.isEmpty()) {
                return Map.of("success", false, "message", "Devices not found: " + missing);
            }

            // Aplicar los estados provistos (ya como DeviceStatusEnum)
            Map<UUID, DeviceStatusEnum> idToState = new HashMap<>();
            for (RestoreDevicesRq.RestoreItem it : items) {
                idToState.put(it.getDeviceId(), it.getState());
            }

            for (Device d : found) {
                DeviceStatusEnum target = idToState.get(d.getId());
                if (target == null) {
                    return Map.of("success", false, "message", "Missing state for device " + d.getId());
                }
                d.setStatus(target);
            }

            deviceRepository.saveAll(found);
            return Map.of("success", true);
        } catch (DataAccessException dae) {
            log.error("Error restaurando devices {}", dae.getMessage(), dae);
            return Map.of("success", false, "message", "DB error");
        } catch (Exception ex) {
            log.error("Error procesando restore items {}", ex.getMessage(), ex);
            return Map.of("success", false, "message", "Invalid request payload");
        }
    }

    /**
     * Construye un mapa con los campos relevantes de la entidad {@link Device}.
     *
     * @param d entidad Device (no nula)
     * @return mapa con claves: id, barcode, brand, name, status, createdAt, updatedAt
     */
    private Map<String, Object> toMap(Device d) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", d.getId());
        m.put("barcode", d.getBarcode());
        m.put("brand", d.getBrand());
        m.put("name", d.getName());
        m.put("status", d.getStatus());
        m.put("createdAt", d.getCreatedAt());
        m.put("updatedAt", d.getUpdatedAt());
        return m;
    }

}