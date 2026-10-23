package ar.edu.unju.fi.arquitecturas.shipmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorRespuestaDto {
    private LocalDateTime marcaDeTiempo;
    private int estado;
    private String error;
    private String mensaje;
    private String ruta;
}