package com.infragest.infra_devices_service.entity;

import com.infragest.infra_devices_service.enums.BulkUploadStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un registro de auditoría de carga masiva.
 *
 * Almacena información sobre las cargas masivas realizadas, incluyendo
 * el archivo original, estado del proceso, tiempos de ejecución y path
 * al archivo de errores generado.
 *
 * Hereda campos comunes (id, timestamps, version) de {@link BaseEntity}.
 *
 * @author bunnystring
 * @since 2026-03-30
 */
@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bulk_upload_log")
public class BulkUploadLog extends BaseEntity{

    /**
     * Nombre original del archivo Excel subido por el usuario.
     */
    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    /**
     * Hash SHA-256 del archivo para detectar duplicados.
     */
    @Column(name = "file_hash", length = 64)
    private String fileHash;

    /**
     * Tipo de operación realizada.
     */
    @Column(name = "operation_type", nullable = false, length = 200)
    private String operationType;

    /**
     * Estado del proceso de carga masiva.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BulkUploadStatusEnum status;

    /**
     * Path del archivo de errores generado.
     */
    @Column(name = "error_log_path", length = 500)
    private String errorLogPath;

    /**
     * Usuario que realizó la carga del archivo.
     */
    @Column(name = "uploaded_by", nullable = false, length = 100)
    private String uploadedBy;

    /**
     * Fecha y hora en que se subió el archivo.
     */
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    /**
     * Fecha y hora en que comenzó el procesamiento.
     */
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    /**
     * Fecha y hora en que finalizó el procesamiento.
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * Tiempo total de procesamiento en milisegundos.
     */
    @Column(name = "processing_time_ms")
    private Long processingTimeMs;

    /**
     * Versión del microservicio que procesó la carga.
     */
    @Column(name = "service_version", length = 20)
    private String serviceVersion;

    /**
     * Dirección IP del cliente que realizó la carga.
     */
    @Column(name = "client_ip", length = 45)
    private String clientIp;

    /**
     * User agent del navegador o cliente que realizó la carga.
     */
    @Column(name = "user_agent")
    private String userAgent;

}
