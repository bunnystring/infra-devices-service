package com.infragest.infra_devices_service.model;

import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Request DTO para reservar o actualizar el estado de varios devices.

 * El campo {@code state} es un {@link DeviceStatusEnum} y será mapeado por Jackson desde su
 * representación en texto (por ejemplo "OCCUPIED").
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveDevicesRq {

    @NotEmpty(message = "La lista de deviceIds no puede estar vacía")
    private List<UUID> deviceIds;

    @NotNull(message = "El estado target (state) es requerido")
    private DeviceStatusEnum state;

}
