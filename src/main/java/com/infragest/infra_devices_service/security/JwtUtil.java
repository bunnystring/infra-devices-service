package com.infragest.infra_devices_service.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 * Utilidad para operaciones básicas con JWT: inicializa la clave secreta,
 * válida tokens y extrae el email (subject).
 *
 * Breve y directo: solo maneja verificación e extracción de subject.
 *
 * @author bunnystring
 * @since 2025-11-07
 */
@Component
public class JwtUtil {

    @Value("${spring.security.oauth2.resourceserver.jwt.secret}")
    private String jwtSecretBase64;

    private Key secretKey;

    /**
     * Inicializa la clave secreta a partir del valor Base64 configurado.
     * Se ejecuta tras la inyección de dependencias.
     */
    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecretBase64);
        this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    /**
     * Extrae el email (subject) de un token JWT.
     *
     * @param token JWT válido
     * @return subject (email) contenido en el token
     */
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Válida la integridad y validez del token JWT.
     *
     * @param token JWT a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
