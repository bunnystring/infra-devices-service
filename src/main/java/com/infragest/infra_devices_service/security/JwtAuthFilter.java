package com.infragest.infra_devices_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

/**
 * Filtro que válida JWT en cada petición y carga la autenticación en el contexto de Spring.
 *
 * - Espera el header Authorization con el esquema "Bearer &lt;token&gt;".
 * - Si el token es válido, extrae el email y crea una {@link Authentication}.
 * - Si falta o es inválido, responde 401 y corta la cadena de filtros.
 *
 * Solo válida el token y pone la autenticación en SecurityContext.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    /**
     * Inyección de dependencia: Configuración jwtUtil.
     */
    private final JwtUtil jwtUtil;

    /**
     * Constructor para la inyección de dependencias.
     * @param jwtUtil
     */
    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Procesa la petición HTTP, válida el token JWT y, en caso válido,
     * establece la {@link Authentication} en él {@link SecurityContextHolder}.
     *
     * Si el header Authorization falta o el token es inválido, devuelve 401 con un mensaje breve.
     *
     * @param request  petición HTTP entrante
     * @param response respuesta HTTP
     * @param filterChain cadena de filtros
     * @throws ServletException sí ocurre un error del servlet
     * @throws IOException si ocurre un error de E/S
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.getEmailFromToken(token);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido o expirado.");
                return;
            }
        } else {
            // Si falta el header Authorization o no es Bearer, rechaza la petición
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header ausente o mal formado.");
            return;
        }
        filterChain.doFilter(request, response);
    }

}
