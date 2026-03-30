package com.infragest.infra_devices_service.repository;

import com.infragest.infra_devices_service.entity.BulkUploadLog;
import com.infragest.infra_devices_service.enums.BulkUploadStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad {@link BulkUploadLog}.
 *
 * Proporciona métodos para consultar y gestionar registros de auditoría
 * de cargas masivas.
 *
 * @author bunnystring
 * @since 2026-03-30
 */
@Repository
public interface BulkUploadLogRepository extends JpaRepository<BulkUploadLog, UUID> {

    /**
     * Busca registros de carga por usuario con paginación.
     *
     * @param uploadedBy nombre del usuario
     * @param pageable configuración de paginación
     * @return página de registros del usuario
     */
    Page<BulkUploadLog> findByUploadedBy(String uploadedBy, Pageable pageable);

    /**
     * Busca registros de carga por estado con paginación.
     *
     * @param status estado de la carga
     * @param pageable configuración de paginación
     * @return página de registros con el estado especificado
     */
    Page<BulkUploadLog> findByStatus(BulkUploadStatusEnum status, Pageable pageable);

    /**
     * Busca registros de carga por tipo de operación con paginación.
     *
     * @param operationType tipo de operación (ej: "DEVICE_BULK_UPLOAD")
     * @param pageable configuración de paginación
     * @return página de registros del tipo especificado
     */
    Page<BulkUploadLog> findByOperationType(String operationType, Pageable pageable);

    /**
     * Busca registros de carga por hash de archivo y fecha de subida posterior a la indicada.
     * Útil para detectar cargas duplicadas recientes.
     *
     * @param fileHash hash SHA-256 del archivo
     * @param uploadedAt fecha límite
     * @return opcional con el registro encontrado
     */
    Optional<BulkUploadLog> findByFileHashAndUploadedAtAfter(String fileHash, LocalDateTime uploadedAt);

    /**
     * Busca registros de carga subidos después de una fecha específica con paginación.
     * Útil para reportes y consultas por rango de fechas.
     *
     * @param uploadedAt fecha límite
     * @param pageable configuración de paginación
     * @return página de registros posteriores a la fecha
     */
    Page<BulkUploadLog> findByUploadedAtAfter(LocalDateTime uploadedAt, Pageable pageable);

    /**
     * Busca registros de carga con múltiples filtros: usuario, estado y fecha.
     *
     * @param uploadedBy nombre del usuario
     * @param status estado de la carga
     * @param uploadedAt fecha límite
     * @param pageable configuración de paginación
     * @return página de registros que cumplen todos los criterios
     */
    Page<BulkUploadLog> findByUploadedByAndStatusAndUploadedAtAfter(
            String uploadedBy,
            BulkUploadStatusEnum status,
            LocalDateTime uploadedAt,
            Pageable pageable
    );

}
