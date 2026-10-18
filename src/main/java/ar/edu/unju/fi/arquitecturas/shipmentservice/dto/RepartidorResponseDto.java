package ar.edu.unju.fi.arquitecturas.shipmentservice.dto;

import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoRepartidor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de respuesta del repartidor")
public class RepartidorResponseDto {

    private UUID id;
    private String nombre;
    private String tipoVehiculo;
    private EstadoRepartidor estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaModificacion;
}