package ar.edu.unju.fi.arquitecturas.shipmentservice.service;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteRequestDTO;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteResponseDTO;
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
    ClienteResponseDTO registrar(ClienteRequestDTO request);

    /**
     * Obtiene el DTO de un cliente por su ID.
     *
     * @param id Identificador UUID.
     * @return Datos del cliente.
     */
    ClienteResponseDTO obtenerPorId(UUID id);

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
    List<ClienteResponseDTO> listarTodos();

    /**
     * Actualiza la información de un cliente.
     *
     * @param id Identificador UUID.
     * @param request Nuevos datos.
     * @return DTO del cliente actualizado.
     */
    ClienteResponseDTO actualizar(UUID id, ClienteRequestDTO request);

    /**
     * Elimina un cliente por su ID.
     *
     * @param id Identificador UUID.
     */
    void eliminar(UUID id);
}