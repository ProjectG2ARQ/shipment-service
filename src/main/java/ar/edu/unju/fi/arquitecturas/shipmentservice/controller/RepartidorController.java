package ar.edu.unju.fi.arquitecturas.shipmentservice.controller;

import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Repartidor;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoRepartidor;
import ar.edu.unju.fi.arquitecturas.shipmentservice.service.RepartidorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/repartidores")
@RequiredArgsConstructor
@Tag(name = "Repartidores", description = "Endpoints para la gestión de repartidores")
public class RepartidorController {

    private final RepartidorService repartidorService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo repartidor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Repartidor registrado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<Repartidor> guardarRepartidor(@RequestBody Repartidor repartidor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repartidorService.guardarRepartidor(repartidor));
    }

    @GetMapping
    @Operation(summary = "Listar todos los repartidores")
    @ApiResponse(responseCode = "200", description = "Listado de repartidores")
    public ResponseEntity<List<Repartidor>> listarRepartidores() {
        return ResponseEntity.ok(repartidorService.obtenerTodosLosRepartidores());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar repartidor por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Repartidor encontrado"),
            @ApiResponse(responseCode = "404", description = "Repartidor no encontrado")
    })
    public ResponseEntity<Repartidor> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(repartidorService.buscarRepartidorPorId(id));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar repartidores por estado")
    @ApiResponse(responseCode = "200", description = "Listado de repartidores filtrados")
    public ResponseEntity<List<Repartidor>> findByEstado(@PathVariable EstadoRepartidor estado) {
        return ResponseEntity.ok(repartidorService.findByEstado(estado));
    }
}