package ar.edu.unju.fi.arquitecturas.shipmentservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@Builder
@Schema(description = "Estructura estándar de respuesta para errores de la API")
public class ErrorResponseDto {

    @Schema(description = "Momento exacto en que ocurrió el error")
    private Instant timestamp;

    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;

    @Schema(description = "Nombre o categoría del error", example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje detallado describiendo la causa del error")
    private String message;

    @Schema(description = "Ruta del endpoint solicitado", example = "/api/v1/envios")
    private String path;

    @Schema(description = "Detalle de errores específicos por campo (en caso de validaciones fallidas)")
    private Map<String, String> fieldErrors;
}