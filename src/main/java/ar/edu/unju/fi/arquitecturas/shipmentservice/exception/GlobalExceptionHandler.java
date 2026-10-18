package ar.edu.unju.fi.arquitecturas.shipmentservice.exception;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EnvioNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDto> handleEnvioNoEncontrado(EnvioNoEncontradoException ex, HttpServletRequest request) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Envío No Encontrado")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CodigoSeguimientoDuplicadoException.class)
    public ResponseEntity<ErrorResponseDto> handleCodigoDuplicado(CodigoSeguimientoDuplicadoException ex, HttpServletRequest request) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value()) // 409 Conflict
                .error("Código de Seguimiento Duplicado")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EstadoEnvioInvalidoException.class)
    public ResponseEntity<ErrorResponseDto> handleEstadoInvalido(EstadoEnvioInvalidoException ex, HttpServletRequest request) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value()) // 422 Unprocessable Entity
                .error("Estado de Envío Inválido")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidacionCampos(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errores = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errores.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value()) // 400 Bad Request
                .error("Error de Validación")
                .message("Existen campos con valores no válidos en la solicitud")
                .path(request.getRequestURI())
                .fieldErrors(errores)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}