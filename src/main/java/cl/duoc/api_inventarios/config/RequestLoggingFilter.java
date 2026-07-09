package cl.duoc.api_inventarios.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String TRACE_ID_KEY = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(TRACE_ID_KEY, traceId);

        try {
            long inicio = System.currentTimeMillis();

            logger.info("Petición recibida: {} {} desde IP: {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());

            filterChain.doFilter(request, response);

            long duracion = System.currentTimeMillis() - inicio;
            int status = response.getStatus();

            if (status >= 500) {
                logger.error("Respuesta enviada con estado: {} en {} ms - {} {}", status, duracion, request.getMethod(), request.getRequestURI());
            } else if (status >= 400) {
                logger.warn("Respuesta enviada con estado: {} en {} ms - {} {}", status, duracion, request.getMethod(), request.getRequestURI());
            } else {
                logger.info("Respuesta enviada con estado: {} en {} ms", status, duracion);
            }
        } finally {
            MDC.remove(TRACE_ID_KEY);
        }
    }
}