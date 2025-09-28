package com.infragest.infra_devices_service.repository;

import com.infragest.infra_devices_service.entity.Device;
import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

    // Find device by barcode (barcode is unique)
    Optional<Device> findByBarcode(String barcode);

    // Find all devices by status
    List<Device> findAllByStatus(DeviceStatusEnum status);

    // Find all devices by multiple statuses (for listing available devices)
    List<Device> findAllByStatusIn(List<DeviceStatusEnum> statuses);
}
