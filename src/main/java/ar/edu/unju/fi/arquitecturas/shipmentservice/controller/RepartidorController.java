package ar.edu.unju.fi.arquitecturas.shipmentservice.controller;

import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Repartidor;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoRepartidor;
import ar.edu.unju.fi.arquitecturas.shipmentservice.service.RepartidorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/repartidores")
@RequiredArgsConstructor
public class RepartidorController {

    private final RepartidorService repartidorService;

    @PostMapping
    public ResponseEntity<Repartidor> guardarRepartidor(@RequestBody Repartidor repartidor) {
        Repartidor nuevoRepartidor = repartidorService.guardarRepartidor(repartidor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRepartidor);
    }

    @GetMapping
    public ResponseEntity<List<Repartidor>> listarRepartidores() {
        List<Repartidor> repartidores = repartidorService.obtenerTodosLosRepartidores();
        return ResponseEntity.ok(repartidores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Repartidor> buscarPorId(@PathVariable UUID id) {
        Repartidor repartidor = repartidorService.buscarRepartidorPorId(id);
        return ResponseEntity.ok(repartidor);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Repartidor>> findByEstado(@PathVariable EstadoRepartidor estado) {
        List<Repartidor> repartidores = repartidorService.findByEstado(estado);
        return ResponseEntity.ok(repartidores);
    }
}