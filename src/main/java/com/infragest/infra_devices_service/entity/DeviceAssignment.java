package com.infragest.infra_devices_service.entity;

import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA que representa la asignación temporal de un dispositivo a una orden.
 *
 * Permite registrar el uso del dispositivo en órdenes específicas y rastrear su estado.
 * Relaciona un dispositivo con una orden mediante una asociación.
 *
 * @author bunnystring
 * @since 2026-01-28
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "device_assignments")
public class DeviceAssignment extends BaseEntity {

    /**
     * ID de la orden que utiliza este dispositivo.
     */
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    /**
     * Dispositivo asignado a la orden.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    /**
     * Estado del dispositivo durante la asignación.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatusEnum status;

    /**
     * Fecha y hora en la que el dispositivo fue asignado a la orden.
     */
    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    /**
     * Fecha y hora en la que el dispositivo fue liberado, si corresponde.
     */
    @Column(name = "released_at")
    private LocalDateTime releasedAt;

}
