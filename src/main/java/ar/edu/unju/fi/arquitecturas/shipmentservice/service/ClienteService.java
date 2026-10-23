package ar.edu.unju.fi.arquitecturas.shipmentservice.service;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Cliente;

import java.util.List;
import java.util.UUID;

/**
 * Servicio para la gestión y administración de clientes.
 */
public interface ClienteService {

    /**
     * Registra un nuevo cliente garantizando la unicidad del email.
     *
     * @param request Datos del cliente.
     * @return Cliente registrado en formato DTO.
     */
    ClienteResponseDto registrar(ClienteRequestDto request);

    /**
     * Obtiene el DTO de un cliente por su ID.
     *
     * @param id Identificador UUID.
     * @return Datos del cliente.
     */
    ClienteResponseDto obtenerPorId(UUID id);

    /**
     * Busca la entidad de dominio Cliente.
     *
     * @param id Identificador UUID.
     * @return Entidad {@link Cliente}.
     */
    Cliente buscarEntidadPorId(UUID id);

    /**
     * Lista todos los clientes registrados.
     *
     * @return Lista de clientes DTO.
     */
    List<ClienteResponseDto> listarTodos();

    /**
     * Actualiza la información de un cliente.
     *
     * @param id Identificador UUID.
     * @param request Nuevos datos.
     * @return DTO del cliente actualizado.
     */
    ClienteResponseDto actualizar(UUID id, ClienteRequestDto request);

    /**
     * Elimina un cliente por su ID.
     *
     * @param id Identificador UUID.
     */
    void eliminar(UUID id);
}