package com.infragest.infra_devices_service.service.impl;

import com.infragest.infra_devices_service.entity.Device;
import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import com.infragest.infra_devices_service.exception.DeviceException;
import com.infragest.infra_devices_service.repository.DeviceRepository;
import com.infragest.infra_devices_service.service.DeviceService;
import com.infragest.infra_devices_service.util.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device saveDevice(Device device) {

        if (deviceRepository.findByBarcode(device.getBarcode()).isPresent()) {
            throw new DeviceException(
                    String.format(MessageException.DEVICE_ALREADY_EXISTS, device.getBarcode()),
                    DeviceException.Type.BAD_REQUEST
            );
        }

        return deviceRepository.save(device);
    }

    @Override
    public Device getDeviceById(UUID id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceException(
                        String.format(MessageException.DEVICE_NOT_FOUND_BY_ID, id),
                        DeviceException.Type.NOT_FOUND
                ));
    }

    @Override
    public Device getDeviceByBarcode(String barcode) {
        return deviceRepository.findByBarcode(barcode)
                .orElseThrow(() -> new DeviceException(
                        String.format(MessageException.DEVICE_NOT_FOUND_BY_BARCODE, barcode),
                        DeviceException.Type.NOT_FOUND
                ));
    }

    @Override
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @Override
    public List<Device> getDevicesByStatus(DeviceStatusEnum status) {
        return deviceRepository.findAllByStatus(status);
    }

    @Override
    public List<Device> getDevicesByStatuses(List<DeviceStatusEnum> statuses) {
        return deviceRepository.findAllByStatusIn(statuses);
    }

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

}