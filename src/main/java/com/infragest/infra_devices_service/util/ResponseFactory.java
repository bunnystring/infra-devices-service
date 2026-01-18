package com.infragest.infra_devices_service.util;

import com.infragest.infra_devices_service.model.ApiResponseDto;

/**
 * Fábrica para crear instancias del DTO {@link ApiResponseDto}.
 *
 * Proporciona métodos estáticos comunes para generar respuestas consistentes
 * a lo largo de los controladores.
 */
public class ResponseFactory {

    private ResponseFactory() {
    }

    /**
     * Crea una respuesta de éxito.
     *
     * @param message Mensaje descriptivo del éxito.
     * @param data Datos adicionales, pueden ser nulos.
     * @param <T> Tipo del contenido adicional.
     * @return Una instancia de {@link ApiResponseDto} indicando éxito.
     */
    public static <T> ApiResponseDto<T> success(String message, T data) {
        return new ApiResponseDto<>(true, message, data);
    }

    /**
     * Crea una respuesta de falla.
     *
     * @param message Mensaje descriptivo del error.
     * @param <T> Tipo del contenido adicional, generalmente nulo.
     * @return Una instancia de {@link ApiResponseDto} indicando fallo.
     */
    public static <T> ApiResponseDto<T> failure(String message) {
        return new ApiResponseDto<>(false, message, null);
    }
}
