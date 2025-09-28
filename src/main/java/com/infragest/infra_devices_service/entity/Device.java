package com.infragest.infra_devices_service.entity;

import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "devices")
public class Device extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(name = "barcode", nullable = false, unique = true)
    private String barcode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatusEnum status;

}
