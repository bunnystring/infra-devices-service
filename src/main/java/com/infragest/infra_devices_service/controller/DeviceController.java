package com.infragest.infra_devices_service.controller;

import com.infragest.infra_devices_service.entity.Device;
import com.infragest.infra_devices_service.enums.DeviceStatusEnum;
import com.infragest.infra_devices_service.model.CreateDeviceRq;
import com.infragest.infra_devices_service.model.DeviceRs;
import com.infragest.infra_devices_service.service.DeviceService;
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
 * Controller REST para operaciones sobre dispositivos.
 *
 * Proporciona endpoints para crear, consultar, filtrar y eliminar dispositivos.
 *
 * @author bunnystring
 * @since 2025-11-07
 */
@Tag(name = "Devices", description = "Operaciones CRUD sobre dispositivos")
@RestController
@RequestMapping("/devices")
public class DeviceController {

    /**
     * deviceService: Servicio de operaciones de dispositivos.
     */
    private final DeviceService deviceService;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param deviceService servicio de dispositivos.
     */
    public DeviceController(
            DeviceService deviceService)
    {
        this.deviceService = deviceService;
    }

    /**
     * Crea un nuevo dispositivo.
     *
     * @param request payload con los datos del dispositivo
     * @return DeviceRs creado
     */
    @Operation(summary = "Crear dispositivo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device creado",
                    content = @Content(schema = @Schema(implementation = DeviceRs.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
    })
    @PostMapping
    public ResponseEntity<DeviceRs> createDevice(@RequestBody @Valid CreateDeviceRq request) {
        return ResponseEntity.ok(deviceService.saveDevice(request));
    }

    /**
     * Obtiene la entidad Device por barcode.
     *
     * @param barcode código de barras
     * @return entidad Device
     */
    @Operation(summary = "Obtener dispositivo por barcode")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device encontrado",
                    content = @Content(schema = @Schema(implementation = Device.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<Device> getDeviceByBarcode(@PathVariable String barcode) {
        Device device = deviceService.getDeviceByBarcode(barcode);
        return ResponseEntity.ok(device);
    }

    /**
     * Obtiene un dispositivo por su id.
     *
     * @param id UUID del dispositivo
     * @return DeviceRs correspondiente
     */
    @Operation(summary = "Obtener dispositivo por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device encontrado",
                    content = @Content(schema = @Schema(implementation = DeviceRs.class))),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeviceRs> getDeviceById(@PathVariable UUID id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    /**
     * Devuelve todos los dispositivos.
     *
     * @return lista de DeviceRs (puede ser vacía)
     */
    @Operation(summary = "Listar todos los dispositivos")
    @ApiResponse(responseCode = "200", description = "Lista de dispositivos",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeviceRs.class))))
    @GetMapping
    public ResponseEntity<List<DeviceRs>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    /**
     * Devuelve dispositivos filtrados por un estado.
     *
     * @param status estado por el que filtrar
     * @return lista de DeviceRs (puede ser vacía)
     */
    @Operation(summary = "Listar dispositivos por estado")
    @ApiResponse(responseCode = "200", description = "Lista filtrada",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeviceRs.class))))
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeviceRs>> getDevicesByStatus(@PathVariable DeviceStatusEnum status) {
        return ResponseEntity.ok(deviceService.getDevicesByStatus(status));
    }

    /**
     * Devuelve dispositivos filtrados por varios estados.
     *
     * @param statuses lista de estados (repetir param en la query)
     * @return lista de DeviceRs (puede ser vacía)
     */
    @Operation(summary = "Listar dispositivos por múltiples estados")
    @ApiResponse(responseCode = "200", description = "Lista filtrada",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeviceRs.class))))
    @GetMapping("/statuses")
    public ResponseEntity<List<DeviceRs>> getDevicesByStatuses(@RequestParam List<DeviceStatusEnum> statuses) {
        return ResponseEntity.ok(deviceService.getDevicesByStatuses(statuses));
    }

    /**
     * Elimina un dispositivo por su id.
     *
     * @param id UUID del dispositivo a eliminar
     * @return 204 No Content
     */
    @Operation(summary = "Eliminar dispositivo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
