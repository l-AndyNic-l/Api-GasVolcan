package cl.duoc.usuarios.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();

        HttpStatus status = ex.getMessage().equals("Usuario no encontrado")
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;

        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, status);
    }
}