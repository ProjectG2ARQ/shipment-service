package ar.edu.unju.fi.arquitecturas.shipmentservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de dirección postal")
public class DireccionDto {

    @NotBlank(message = "La calle es obligatoria")
    @Schema(example = "Lavalle")
    private String calle;

    @NotBlank(message = "El número es obligatorio")
    @Schema(example = "340")
    private String numero;

    @NotBlank(message = "La ciudad es obligatoria")
    @Schema(example = "San Salvador de Jujuy")
    private String ciudad;

    @NotBlank(message = "El código postal es obligatorio")
    @Schema(example = "4600")
    private String codigoPostal;
}