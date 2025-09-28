package com.infragest.infra_devices_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // @valid desde el controlador cuando el dto tiene validaciones

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Validation Error",
                        "message", message
                ));
    }

    // Maneja tu excepción personalizada DeviceException
    @ExceptionHandler(DeviceException.class)
    public ResponseEntity<?> handleDeviceException(DeviceException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ex.getType() == DeviceException.Type.NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", status.value(),
                        "error", "Device Error",
                        "type", ex.getType(),
                        "message", ex.getMessage()
                ));
    }

    // Maneja cualquier otra exception no controlada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericsException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "error", "Internal Server Error",
                        "message", ex.getMessage()
                ));
    }
}
