package com.infragest.infra_devices_service.entity;

import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa un dispositivo.
 *
 * Contiene los datos básicos del dispositivo: nombre, marca, barcode (único) y estado.
 * Hereda campos comunes (id, timestamps, etc.) de {@link BaseEntity}.
 *
 * @author bunnystring
 * @since 2025-11-07
 */
@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "devices")
public class Device extends BaseEntity{

    /**
     * Nombre del dispositivo.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Marca del dispositivo.
     */
    @Column(nullable = false)
    private String brand;

    /**
     * Código de barras del dispositivo. Valor único y no nulo.
     */
    @Column(name = "barcode", nullable = false, unique = true)
    private String barcode;

    /**
     * Estado del dispositivo. Se persiste como STRING en la base de datos.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatusEnum status;

}
