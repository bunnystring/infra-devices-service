package com.infragest.infra_devices_service.controller;

import com.infragest.infra_devices_service.entity.DeviceAssignment;
import com.infragest.infra_devices_service.model.DeviceAssignmentDto;
import com.infragest.infra_devices_service.service.DeviceAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST para operaciones sobre las asignaciones de dispositivos.
 *
 * Proporciona endpoints para consultar asignaciones activas e históricas.
 * Documentado con Swagger para una descripción detallada de los endpoints.
 *
 * @author bunnystring
 * @since 2026-01-31
 */
@Tag(name = "DevicesAssignment", description = "Operaciones CRUD sobre las asignaciones de dispositivos")
@RestController
@RequestMapping("/devices-assignments")
public class DeviceAssignmentController {

    /**
     * Inyección de la dependencia: deviceAssignmentService.
     */
    private final DeviceAssignmentService deviceAssignmentService;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param deviceAssignmentService servicio de dispositivos.
     */
    public DeviceAssignmentController(DeviceAssignmentService deviceAssignmentService) {
        this.deviceAssignmentService = deviceAssignmentService;
    }

    /**
     * Verifica si un dispositivo tiene una asignación activa.
     *
     * @param deviceId UUID del dispositivo.
     * @return {@code true} si tiene una asignación activa, {@code false} en caso contrario.
     */
    @Operation(summary = "Verifica si un dispositivo tiene una asignación activa",
            description = "Devuelve `true` si el dispositivo tiene una asignación activa (releasedAt es null), `false` en caso contrario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta exitosa: el dispositivo tiene/no tiene una asignación activa."),
            @ApiResponse(responseCode = "404", description = "El dispositivo con el ID proporcionado no existe.",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{deviceId}/active")
    public ResponseEntity<Boolean> hasActiveAssignment(@PathVariable UUID deviceId) {
        return ResponseEntity.ok(deviceAssignmentService.hasActiveAssignment(deviceId));
    }

    /**
     * Obtiene el historial de asignaciones de un dispositivo.
     *
     * @param deviceId UUID del dispositivo.
     * @return Lista de asignaciones históricas del dispositivo, incluyendo datos como deviceId, orderId, assignedAt, y releasedAt.
     */
    @Operation(summary = "Obtiene el historial de asignaciones finalizadas de un dispositivo",
            description = "Devuelve una lista de asignaciones que incluyen `deviceId`, `orderId`, `assignedAt`, y `releasedAt`.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de asignaciones devuelta correctamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeviceAssignment.class))),
            @ApiResponse(responseCode = "404", description = "El dispositivo con el ID proporcionado no existe o no tiene asignaciones históricas.",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{deviceId}/history")
    public ResponseEntity<List<DeviceAssignmentDto>> getDeviceAssignmentHistory(@PathVariable UUID deviceId) {

        return ResponseEntity.ok(deviceAssignmentService.getDeviceAssignmentHistory(deviceId));
    }
}
