package ar.edu.unju.fi.arquitecturas.shipmentservice.service;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteRequestDTO;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteResponseDTO;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Cliente;

import java.util.List;
import java.util.UUID;

public interface ClienteService {
    ClienteResponseDTO registrar(ClienteRequestDTO request);
    ClienteResponseDTO obtenerPorId(UUID id);
    Cliente buscarEntidadPorId(UUID id);
    List<ClienteResponseDTO> listarTodos();
    ClienteResponseDTO actualizar(UUID id, ClienteRequestDTO request);
    void eliminar(UUID id);
}