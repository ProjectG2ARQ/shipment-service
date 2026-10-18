package ar.edu.unju.fi.arquitecturas.shipmentservice.dto;

import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoRepartidor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos para el registro de un repartidor")
public class RepartidorRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(example = "Carlos Gómez")
    private String nombre;

    @NotBlank(message = "El tipo de vehículo es obligatorio")
    @Schema(example = "MOTO")
    private String tipoVehiculo;

    @NotNull(message = "El estado es obligatorio")
    @Schema(example = "ACTIVO")
    private EstadoRepartidor estado;
}