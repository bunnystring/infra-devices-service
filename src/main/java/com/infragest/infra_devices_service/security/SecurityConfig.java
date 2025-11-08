package com.infragest.infra_devices_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad para el servicio de dispositivos.
 *
 * @author bunnystring
 * @since 2025-11-08
 */
@Configuration
public class SecurityConfig {

    /**
     * Filtro que valida tokens JWT y construye la {@code Authentication} en el SecurityContext.
     * Se registra antes del filtro de autenticación por usuario/contraseña para asegurar que
     * las peticiones portadoras de JWT queden autenticadas.
     */
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param jwtAuthFilter filtro responsable de validar y parsear el JWT de la cabecera Authorization
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Construye y devuelve la cadena de filtros de seguridad.
     *
     * @param http objeto {@link HttpSecurity} proporcionado por Spring Security
     * @return la {@link SecurityFilterChain} construida y lista para registrar como bean
     * @throws Exception si ocurre algún error al configurar la cadena de filtros
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()   // TODAS las rutas deben ir autenticadas por token
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
