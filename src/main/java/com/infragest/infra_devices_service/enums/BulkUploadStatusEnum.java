package com.infragest.infra_devices_service.enums;

/**
 * Enumeración para los estados de una carga masiva.
 *
 * @author bunnystring
 * @since 2026-03-30
 */
public enum BulkUploadStatusEnum {
    /**
     * La carga está pendiente de procesamiento.
     */
    PENDING,

    /**
     * La carga se está procesando actualmente.
     */
    PROCESSING,

    /**
     * La carga se completó exitosamente sin errores.
     */
    COMPLETED,

    /**
     * La carga se completó pero con advertencias (algunos registros fallaron).
     */
    COMPLETED_WITH_WARNINGS,

    /**
     * La carga falló completamente.
     */
    FAILED
}
