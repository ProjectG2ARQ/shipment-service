package ar.edu.unju.fi.arquitecturas.shipmentservice.controller;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones CRUD para la gestión de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente registrado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "422", description = "Conflicto o email duplicado")
    })
    public ResponseEntity<ClienteResponseDto> registrar(@Valid @RequestBody ClienteRequestDto request) {
        ClienteResponseDto nuevoCliente = clienteService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un cliente por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ClienteResponseDto> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos los clientes registrados")
    @ApiResponse(responseCode = "200", description = "Listado de clientes")
    public ResponseEntity<List<ClienteResponseDto>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar los datos de un cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<ClienteResponseDto> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ClienteRequestDto request) {
        return ResponseEntity.ok(clienteService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}