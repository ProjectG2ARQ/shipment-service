package ar.edu.unju.fi.arquitecturas.shipmentservice.controller;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Envio;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoEnvio;
import ar.edu.unju.fi.arquitecturas.shipmentservice.service.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/envios")
@RequiredArgsConstructor
@Tag(name = "Envíos", description = "Endpoints para la gestión del ciclo de vida de los envíos")
public class EnvioController {

    private final EnvioService envioService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo envío")
    public ResponseEntity<EnvioResponseDto> crearEnvio(@Valid @RequestBody EnvioRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.crearEnvio(requestDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un envío por ID")
    public ResponseEntity<EnvioResponseDto> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(envioService.obtenerPorId(id));
    }

    @GetMapping("/tracking/{codigoSeguimiento}")
    @Operation(summary = "Consultar un envío por código de seguimiento")
    public ResponseEntity<EnvioResponseDto> obtenerPorTracking(@PathVariable String codigoSeguimiento) {
        return ResponseEntity.ok(envioService.obtenerPorCodigoSeguimiento(codigoSeguimiento));
    }

    @GetMapping
    @Operation(summary = "Listar envíos por estado")
    public ResponseEntity<List<EnvioResponseDto>> listarPorEstado(@RequestParam EstadoEnvio estado) {
        return ResponseEntity.ok(envioService.listarPorEstado(estado));
    }

    @GetMapping("/destino")
    @Operation(summary = "Listar envíos por ciudad de destino")
    public ResponseEntity<List<EnvioResponseDto>> listarPorCiudadDestino(@RequestParam String ciudad) {
        return ResponseEntity.ok(envioService.listarPorCiudadDestino(ciudad));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar el estado de un envío")
    public ResponseEntity<EnvioResponseDto> actualizarEstado(
            @PathVariable UUID id,
            @RequestParam EstadoEnvio nuevoEstado) {
        return ResponseEntity.ok(envioService.actualizarEstado(id, nuevoEstado));
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar un envío en estado PENDIENTE")
    public ResponseEntity<Void> cancelarEnvio(@PathVariable UUID id) {
        envioService.cancelarEnvio(id);
        return ResponseEntity.noContent().build();
    }
    // yo agregue est o pa que no nos confundamos(soy jorge)
    @PatchMapping("/{envioId}/repartidores/{repartidorId}")
    @Operation(summary = "Asignar un repartidor activo a un envío pendiente")
    public ResponseEntity<EnvioResponseDto> asignarRepartidor(
            @PathVariable UUID envioId,
            @PathVariable UUID repartidorId) {

        return ResponseEntity.ok(envioService.asignarRepartidorAEnvio(envioId, repartidorId));
    }
}