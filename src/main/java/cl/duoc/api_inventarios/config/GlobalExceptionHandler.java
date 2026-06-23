package cl.duoc.api_inventarios.config;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> Respuesta(HttpStatus status, String Mensaje, WebRequest request) {
        Map<String, Object> budy = new HashMap<>();
        body.put("Fecha", LocalDateTime.now());
        body.put("HTTP Status", status.value());
        body.put("Error", status.getReasonPhrase)
        body.put("Mensaje ", Mensaje);
        body.put("URI - Ruta", request.getDescription());
        return new Response Entity;

    }

    //Error 400 BadRequest
    @ExceptionHandler (IllegalAccessException ex, WebRequest request){
        return Respuesta(HttpStatus.BAD.REQUEST, ex.getMessage().request);
    }

    //Falta crear el 404


    //Aqui cae el error 500 y todo lo demas
    @ExeptionHandler (Exception.class)
    public ResponseEntity <Object> GlobalExeption(Exception ex, WebRequest request);
        return Respuesta(HttpStatus.INTERNAL_SEVER_ERROR,  "Ocurrio algo malo", request);


}
