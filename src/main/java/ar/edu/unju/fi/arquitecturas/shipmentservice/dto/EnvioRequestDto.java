package ar.edu.unju.fi.arquitecturas.shipmentservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos para registrar un nuevo envío")
public class EnvioRequestDto {

    @NotBlank(message = "El código de seguimiento es obligatorio")
    @Schema(example = "TRACK-JUJ-001")
    private String codigoSeguimiento;

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe ser mayor a 0")
    @Schema(example = "2.5")
    private Double peso;

    @NotNull(message = "El ancho es obligatorio")
    @Positive(message = "El ancho debe ser mayor a 0")
    @Schema(example = "10.0")
    private Double ancho;

    @NotNull(message = "El alto es obligatorio")
    @Positive(message = "El alto debe ser mayor a 0")
    @Schema(example = "15.0")
    private Double alto;

    @NotNull(message = "El largo es obligatorio")
    @Positive(message = "El largo debe ser mayor a 0")
    @Schema(example = "20.0")
    private Double largo;

    @NotNull(message = "La dirección de origen es obligatoria")
    @Valid
    private DireccionDto origen;

    @NotNull(message = "La dirección de destino es obligatoria")
    @Valid
    private DireccionDto destino;
}