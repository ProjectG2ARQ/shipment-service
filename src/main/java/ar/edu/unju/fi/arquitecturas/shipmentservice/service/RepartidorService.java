package ar.edu.unju.fi.arquitecturas.shipmentservice.service;

import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Repartidor;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoRepartidor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepartidorService {

    Repartidor guardarRepartidor(Repartidor repartidor);

    List<Repartidor> obtenerTodosLosRepartidores();

    Repartidor buscarRepartidorPorId(UUID id);

    List<Repartidor> findByEstado(EstadoRepartidor estado);
}