package ar.edu.unju.fi.arquitecturas.shipmentservice.dto;

import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoEnvio;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta detallada del envío")
public class EnvioResponseDto {

    private UUID id;
    private String codigoSeguimiento;

    @Schema(example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    private UUID clienteId;

    private Double peso;
    private Double ancho;
    private Double alto;
    private Double largo;
    private Double tarifa;
    private EstadoEnvio estado;
    private DireccionDto origen;
    private DireccionDto destino;

    private UUID repartidorId;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaModificacion;
}