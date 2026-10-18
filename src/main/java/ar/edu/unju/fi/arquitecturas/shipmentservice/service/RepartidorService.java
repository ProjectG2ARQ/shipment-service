package ar.edu.unju.fi.arquitecturas.shipmentservice.service;

import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Repartidor;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoRepartidor;

import java.util.List;
import java.util.UUID;

/**
 * Servicio para la administración y consulta de repartidores.
 */
public interface RepartidorService {

    /**
     * Registra un nuevo repartidor.
     *
     * @param repartidor Entidad con los datos del repartidor.
     * @return Entidad persistida.
     */
    Repartidor guardarRepartidor(Repartidor repartidor);

    /**
     * Obtiene el total de repartidores registrados.
     *
     * @return Lista de repartidores.
     */
    List<Repartidor> obtenerTodosLosRepartidores();

    /**
     * Localiza un repartidor por su identificador UUID.
     *
     * @param id Identificador del repartidor.
     * @return Repartidor localizado.
     */
    Repartidor buscarRepartidorPorId(UUID id);

    /**
     * Filtra repartidores según su estado operativo.
     *
     * @param estado Estado ACTIVO o INACTIVO.
     * @return Lista de repartidores coincidentes.
     */
    List<Repartidor> findByEstado(EstadoRepartidor estado);
}