package com.infragest.infra_devices_service.controller;

import com.infragest.infra_devices_service.entity.DeviceAssignment;
import com.infragest.infra_devices_service.model.DeviceAssignmentActiveRs;
import com.infragest.infra_devices_service.model.DeviceAssignmentDto;
import com.infragest.infra_devices_service.model.DevicesBatchRq;
import com.infragest.infra_devices_service.service.DeviceAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Verifica si los dispositivos especificados tienen una asignación activa.
     *
     * @param devicesBatchRq Objeto que contiene una lista de UUIDs de dispositivos a consultar.
     * @return {@link ResponseEntity} con una lista de objetos {@link DeviceAssignmentActiveRs},
     *         cada uno indicando el deviceId consultado y si tiene una asignación activa.
     */
    @Operation(
            summary = "Verifica si varios dispositivos tienen asignación activa",
            description = "Recibe una lista de UUIDs de dispositivos y devuelve un array de objetos con el estado de asignación activa (`active=true`) o inactiva (`active=false`). Si un dispositivo no existe, se devuelve igual con `active=false`."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Respuesta exitosa: array indicando para cada dispositivo si tiene asignación activa.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeviceAssignmentActiveRs.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida (por ejemplo, lista vacía o mal formada).",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    })
    @PostMapping("/devices/active")
    public ResponseEntity<List<DeviceAssignmentActiveRs>> hasActiveAssignment(
            @RequestBody @Valid DevicesBatchRq devicesBatchRq) {
        return ResponseEntity.ok(deviceAssignmentService.hasActiveAssignment(devicesBatchRq));
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
