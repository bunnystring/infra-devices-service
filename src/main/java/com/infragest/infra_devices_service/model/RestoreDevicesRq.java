package com.infragest.infra_devices_service.model;

import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Request DTO para restaurar estados originales de devices.
 *
 * {@code state} es un {@link DeviceStatusEnum} y será mapeado desde su representación en texto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestoreDevicesRq {

    @NotEmpty(message = "La lista de items no puede estar vacía")
    @Valid
    private List<RestoreItem> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RestoreItem {
        @NotNull(message = "deviceId es requerido")
        private UUID deviceId;

        @NotNull(message = "state es requerido")
        private DeviceStatusEnum state;
    }
}
