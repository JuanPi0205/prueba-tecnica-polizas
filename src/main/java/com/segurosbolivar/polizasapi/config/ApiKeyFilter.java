package com.segurosbolivar.polizasapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends  OncePerRequestFilter {

    private static final String API_KEY_HEADER = "x-api-key";
    private static final String API_KEY_VALUE = "123456";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Excluimos la ruta del mock para que el mismo servidor pueda consumirla sin problemas
        if (request.getRequestURI().startsWith("/core-mock")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(API_KEY_HEADER);

        if (API_KEY_VALUE.equals(apiKey)) {
            // Si el token es válido, continúa el flujo normal
            filterChain.doFilter(request, response);
        } else {
            // Si no es válido o no viene, devolvemos un 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Acceso denegado. Header x-api-key invalido o ausente.");
        }
    }
}
