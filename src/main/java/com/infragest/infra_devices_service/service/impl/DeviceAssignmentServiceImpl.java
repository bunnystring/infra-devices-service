package com.infragest.infra_devices_service.service.impl;

import com.infragest.infra_devices_service.entity.Device;
import com.infragest.infra_devices_service.entity.DeviceAssignment;
import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import com.infragest.infra_devices_service.exception.DeviceException;
import com.infragest.infra_devices_service.model.DeviceAssignmentDto;
import com.infragest.infra_devices_service.repository.DeviceAssignmentRepository;
import com.infragest.infra_devices_service.repository.DeviceRepository;
import com.infragest.infra_devices_service.service.DeviceAssignmentService;
import com.infragest.infra_devices_service.util.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceAssignmentServiceImpl implements DeviceAssignmentService {

    private final DeviceAssignmentRepository deviceAssignmentRepository;
    private final DeviceRepository deviceRepository;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param deviceAssignmentRepository Repositorio de asignaciones.
     * @param deviceRepository Repositorio de dispositivos.
     */
    public DeviceAssignmentServiceImpl(DeviceAssignmentRepository deviceAssignmentRepository,
                                       DeviceRepository deviceRepository) {
        this.deviceAssignmentRepository = deviceAssignmentRepository;
        this.deviceRepository = deviceRepository;
    }

    /**
     * Asigna un dispositivo a una orden si está disponible y no tiene una asignación activa.
     *
     * @param orderId Identificador único de la orden.
     * @param deviceId Identificador único del dispositivo.
     */
    @Override
    @Transactional
    public void assignDeviceToOrder(UUID orderId, UUID deviceId) {

        // Busca una asignación activa para el deviceId
        Optional<DeviceAssignment> activeAssignment = deviceAssignmentRepository.findByDeviceIdAndReleasedAtIsNull(deviceId);

        // Verificar si el dispositivo tiene una asignación activa
        if (activeAssignment.isPresent()) {
            throw new DeviceException(
                    String.format(MessageException.DEVICE_ALREADY_ASSIGNED, deviceId),
                    DeviceException.Type.BAD_REQUEST
            );
        }

        // Recuperar el dispositivo
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceException(
                        String.format(MessageException.DEVICE_NOT_FOUND_BY_ID, deviceId),
                        DeviceException.Type.NOT_FOUND
                ));

        // Validar que el dispositivo esté en GOOD_CONDITION
        if (!device.getStatus().equals(DeviceStatusEnum.GOOD_CONDITION)) {
            throw new DeviceException(
                    String.format(MessageException.DEVICE_NOT_AVAILABLE_FOR_ASSIGNMENT, deviceId),
                    DeviceException.Type.BAD_REQUEST
            );
        }

        // Crear la nueva asignación
        DeviceAssignment assignment = DeviceAssignment.builder()
                .orderId(orderId)
                .device(device)
                .status(DeviceStatusEnum.OCCUPIED)
                .assignedAt(LocalDateTime.now())
                .build();

        deviceAssignmentRepository.save(assignment);
        log.info("Asignación creada para el dispositivo {} en la orden {}", deviceId, orderId);

        // Actualizar el estado del dispositivo a OCCUPIED
        device.setStatus(DeviceStatusEnum.OCCUPIED);
        deviceRepository.save(device);
    }

    /**
     * Libera un dispositivo asociado a una orden específica, finalizando la asignación activa.
     *
     * @param deviceId Identificador único del dispositivo.
     */
    @Override
    @Transactional
    public void releaseDeviceFromOrder(UUID deviceId, DeviceStatusEnum status) {

        // Buscar asignación activa con bloqueo pesimista
        DeviceAssignment assignment = deviceAssignmentRepository.findWithLockByDeviceIdAndReleasedAtIsNull(deviceId)
                .orElseThrow(() -> new DeviceException(
                        String.format(MessageException.DEVICE_ASSIGNMENT_NOT_FOUND, deviceId),
                        DeviceException.Type.NOT_FOUND
                ));

        // Finalizar la asignación
        assignment.setReleasedAt(LocalDateTime.now());
        assignment.setStatus(status);
        deviceAssignmentRepository.save(assignment);
        log.info("Assignment released for device {}", deviceId);

        // Cambiar el estado del dispositivo a GOOD_CONDITION
        Device device = assignment.getDevice();
        device.setStatus(DeviceStatusEnum.GOOD_CONDITION);
        deviceRepository.save(device);
        log.info("Device {} status set to GOOD_CONDITION", deviceId);
    }

    /**
     * Obtiene todas las asignaciones históricas (liberadas) de un dispositivo.
     *
     * @param deviceId Identificador único del dispositivo.
     * @return Lista de asignaciones históricas del dispositivo.
     */
    @Override
    public List<DeviceAssignmentDto> getDeviceAssignmentHistory(UUID deviceId) {

        // Buscar asignaciones históricas
        List<DeviceAssignment> assignments = deviceAssignmentRepository.findAllByDeviceIdAndReleasedAtIsNotNullOrderByReleasedAtDesc(deviceId);

        // Registrar log con el tamaño de las asignaciones encontradas
        log.info("Se encontraron {} asignaciones históricas para el dispositivo {}", assignments.size(), deviceId);

        // Mapear cada asignación a un DTO y devolver la lista resultante
        return assignments.stream()
                .map(assignment -> DeviceAssignmentDto.builder()
                        .deviceId(assignment.getDevice().getId())
                        .deviceName(assignment.getDevice().getName())
                        .orderId(assignment.getOrderId())
                        .deviceStatus(assignment.getStatus())
                        .assignedAt(assignment.getAssignedAt())
                        .releasedAt(assignment.getReleasedAt())
                        .build()
                )
                .collect(Collectors.toList()); // Colectar como lista
    }

    /**
     * Verifica si un dispositivo tiene una asignación activa (releasedAt es null).
     *
     * @param deviceId Identificador único del dispositivo.
     * @return {@code true} si el dispositivo tiene una asignación activa, {@code false} en caso contrario.
     */
    @Override
    public boolean hasActiveAssignment(UUID deviceId) {
        boolean hasActive = deviceAssignmentRepository.findByDeviceIdAndReleasedAtIsNull(deviceId).isPresent();
        log.info("El dispositivo {} {} una asignación activa.", deviceId, hasActive ? "tiene" : "no tiene");
        return hasActive;
    }
}
