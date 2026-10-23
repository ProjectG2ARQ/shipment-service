package ar.edu.unju.fi.arquitecturas.shipmentservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos detallados de respuesta del cliente")
public class ClienteResponseDto {

    @Schema(example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    private UUID id;

    @Schema(example = "Juan Pérez")
    private String nombre;

    @Schema(example = "juan.perez@example.com")
    private String email;

    @Schema(example = "+54 388 4123456")
    private String telefono;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}