package com.infragest.infra_devices_service.model;

import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDevicesStateRq {

    /**
     * Lista de IDs de los dispositivos a reservar.
     */
    @NotEmpty(message = "La lista de IDs no puede estar vacía.")
    private List<UUID> deviceIds;

    /**
     * Nuevo estado para los dispositivos.
     */
    @NotNull(message = "Debe especificarse un nuevo estado para los dispositivos.")
    private DeviceStatusEnum state;
}
