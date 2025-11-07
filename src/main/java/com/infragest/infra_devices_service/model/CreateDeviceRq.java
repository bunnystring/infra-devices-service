package com.infragest.infra_devices_service.model;

import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


/**
 * DTO para la creación de un dispositivo.
 * Contiene los campos mínimos necesarios para crear un Device.
 *
 * @author bunnystring
 * @since 2025-11-06
 * @version 1.1
 */
@Data
public class CreateDeviceRq {

    /**
     * Nombre descriptivo del dispositivo (máx. 100 chars).
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String name;

    /**
     * Marca del dispositivo (máx. 100 chars).
     */
    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 100, message = "La marca no puede superar 100 caracteres")
    private String brand;

    /**
     * Código de barras único del dispositivo (máx. 100 chars).
     */
    @NotBlank(message = "El código de barras es obligatorio")
    @Size(max = 100, message = "El código de barras no puede superar 100 caracteres")
    private String barcode;

    /**
     * Estado inicial del dispositivo (no nulo).
     */
    @NotNull(message = "El estado del dispositivo es obligatorio")
    private DeviceStatusEnum status;

}
