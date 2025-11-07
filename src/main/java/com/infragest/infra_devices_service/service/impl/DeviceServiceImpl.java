package com.infragest.infra_devices_service.service.impl;

import com.infragest.infra_devices_service.entity.Device;
import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import com.infragest.infra_devices_service.exception.DeviceException;
import com.infragest.infra_devices_service.model.CreateDeviceRq;
import com.infragest.infra_devices_service.model.DeviceRs;
import com.infragest.infra_devices_service.repository.DeviceRepository;
import com.infragest.infra_devices_service.service.DeviceService;
import com.infragest.infra_devices_service.util.MessageException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

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
     * deviceRepository: Repositorio de dispositivos.
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
                        String.format(MessageException.DEVICE_NOT_FOUND_BY_ID, id),
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
                        ? device.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                        : null)
                .updatedAt(device.getUpdatedAt() != null
                        ? device.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant()
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

}