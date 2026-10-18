package ar.edu.unju.fi.arquitecturas.shipmentservice.service;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.RepartidorRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.RepartidorResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Repartidor;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoRepartidor;

import java.util.List;
import java.util.UUID;

/**
 * Servicio para la administración y consulta de repartidores.
 */
public interface RepartidorService {

    /**
     * Registra un nuevo repartidor verificando que no exista duplicidad de nombre.
     *
     * @param requestDto Datos del repartidor a registrar.
     * @return Repartidor persistido en formato DTO.
     */
    RepartidorResponseDto guardarRepartidor(RepartidorRequestDto requestDto);

    /**
     * Obtiene el listado de todos los repartidores registrados.
     *
     * @return Lista de repartidores en formato DTO.
     */
    List<RepartidorResponseDto> obtenerTodosLosRepartidores();

    /**
     * Localiza un repartidor por su identificador UUID retornando su DTO.
     *
     * @param id Identificador UUID del repartidor.
     * @return DTO del repartidor localizado.
     */
    RepartidorResponseDto buscarRepartidorPorId(UUID id);

    /**
     * Busca la entidad de dominio interna Repartidor.
     *
     * @param id Identificador UUID del repartidor.
     * @return Entidad {@link Repartidor}.
     */
    Repartidor buscarEntidadPorId(UUID id);

    /**
     * Filtra repartidores según su estado operativo.
     *
     * @param estado Estado ACTIVO o INACTIVO.
     * @return Lista de repartidores coincidentes en formato DTO.
     */
    List<RepartidorResponseDto> findByEstado(EstadoRepartidor estado);
}