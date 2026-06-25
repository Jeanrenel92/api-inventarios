package cl.duoc.api_inventarios.config;

import lombok.extern.slf4j.Slf4j; // <-- Importación de Lombok
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ResponseEntity<Object> construirRespuesta(HttpStatus status, String mensaje, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("fecha", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("mensaje", mensaje);
        body.put("ruta", request.getDescription(false));

        return new ResponseEntity<>(body, status);
    }

    // Error 400 - Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex, WebRequest request) {
        // Usamos WARN porque es un error del cliente (envió algo mal), no del servidor
        log.warn("Error 400 - Bad Request: {} | Ruta: {}", ex.getMessage(), request.getDescription(false));
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // Error 404 - Not Found
    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        // Usamos WARN porque simplemente buscaron algo que no existe
        log.warn("Error 404 - Not Found: Recurso no encontrado | Ruta: {}", request.getDescription(false));
        return construirRespuesta(HttpStatus.NOT_FOUND, "El recurso solicitado no existe", request);
    }

    // Error 500 - Internal Server Error (Atrapa cualquier otra excepción no controlada)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Error 500 - Falla interna crítica en la ruta: {}", request.getDescription(false), ex);

        return construirRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado en el servidor", request);
    }
}