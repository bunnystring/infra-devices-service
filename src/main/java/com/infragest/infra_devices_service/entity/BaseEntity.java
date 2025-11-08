package com.infragest.infra_devices_service.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Superclase mapeada para entidades JPA que proporciona campos comunes.
 * Incluye callbacks de persistencia para inicializar timestamps.
 *
 * @author bunnystring
 * @since 2025-11-07
 */
@Data
@MappedSuperclass
public class BaseEntity {

    /**
     * Identificador único (UUID) generado por Hibernate.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Fecha y hora de creación. Se inicializa en prePersist si es null.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última actualización. Se actualiza en preUpdate.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Versión para control de concurrencia optimista.
     */
    @Version
    @Column(name = "version")
    private Long version;

    /**
     * Callback antes de persistir: asegura createdAt.
     */
    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    /**
     * Callback antes de actualizar: actualiza updatedAt.
     */
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
