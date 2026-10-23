package ar.edu.unju.fi.arquitecturas.shipmentservice.service.impl;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.NegocioException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Cliente;
import ar.edu.unju.fi.arquitecturas.shipmentservice.repository.ClienteRepository;
import ar.edu.unju.fi.arquitecturas.shipmentservice.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public ClienteResponseDto registrar(ClienteRequestDto request) {
        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new NegocioException("Ya existe un cliente registrado con el email: " + request.getEmail());
        }

        Cliente cliente = Cliente.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .build();

        Cliente guardado = clienteRepository.save(cliente);
        return mapearADTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDto obtenerPorId(UUID id) {
        Cliente cliente = buscarEntidadPorId(id);
        return mapearADTO(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarEntidadPorId(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDto> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Override
    @Transactional
    public ClienteResponseDto actualizar(UUID id, ClienteRequestDto request) {
        Cliente cliente = buscarEntidadPorId(id);

        if (clienteRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new NegocioException("El email " + request.getEmail() + " ya está en uso por otro cliente");
        }

        cliente.setNombre(request.getNombre());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());

        Cliente actualizado = clienteRepository.save(cliente);
        return mapearADTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(UUID id) {
        Cliente cliente = buscarEntidadPorId(id);
        clienteRepository.delete(cliente);
    }

    private ClienteResponseDto mapearADTO(Cliente cliente) {
        return ClienteResponseDto.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .createdAt(cliente.getCreatedAt())
                .updatedAt(cliente.getUpdatedAt())
                .build();
    }
}