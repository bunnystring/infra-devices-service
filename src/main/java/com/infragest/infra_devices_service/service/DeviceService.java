package com.infragest.infra_devices_service.service;

import com.infragest.infra_devices_service.entity.Device;
import com.infragest.infra_devices_service.enums.DeviceStatusEnum;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceService {

    Device saveDevice(Device device);

    Device getDeviceById(UUID id);

    Device getDeviceByBarcode(String barcode);

    List<Device> getAllDevices();

    List<Device> getDevicesByStatus(DeviceStatusEnum status);

    List<Device> getDevicesByStatuses(List<DeviceStatusEnum> statuses);

    void deleteDevice(UUID id);
}
