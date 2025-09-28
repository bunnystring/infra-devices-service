package com.infragest.infra_devices_service.controller;

import com.infragest.infra_devices_service.entity.Device;
import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import com.infragest.infra_devices_service.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        return ResponseEntity.ok(deviceService.saveDevice(device));
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<Device> getDeviceByBarcode(@PathVariable String barcode) {
        Device device = deviceService.getDeviceByBarcode(barcode);
        return ResponseEntity.ok(device);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable UUID id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Device>> getDevicesByStatus(@PathVariable DeviceStatusEnum status) {
        List<Device> devices = deviceService.getDevicesByStatus(status);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<Device>> getDevicesByStatuses(@RequestParam List<DeviceStatusEnum> statuses) {
        List<Device> devices = deviceService.getDevicesByStatuses(statuses);
        return ResponseEntity.ok(devices);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
