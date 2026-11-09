package ar.edu.unju.fi.arquitecturas.shipmentservice.service;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.NegocioException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Cliente;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoCliente;
import ar.edu.unju.fi.arquitecturas.shipmentservice.repository.ClienteRepository;
import ar.edu.unju.fi.arquitecturas.shipmentservice.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    @DisplayName("Debe registrar un cliente correctamente si el email no existe")
    void registrar_EmailNoExiste_GuardaExitosamente() {
        ClienteRequestDto request = ClienteRequestDto.builder()
                .nombre("Lucas")
                .email("lucas@gmail.com")
                .telefono("388123456")
                .build();

        Cliente entidadGuardada = Cliente.builder()
                .id(UUID.randomUUID())
                .nombre(request.getNombre())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .estado(EstadoCliente.ACTIVO)
                .createdAt(LocalDateTime.now())
                .build();

        when(clienteRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(entidadGuardada);

        ClienteResponseDto resultado = clienteService.registrar(request);

        assertNotNull(resultado);
        assertEquals(request.getEmail(), resultado.getEmail());
        assertEquals(EstadoCliente.ACTIVO, resultado.getEstado());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Debe lanzar NegocioException al intentar registrar un email repetido")
    void registrar_EmailExistente_LanzaExcepcion() {
        ClienteRequestDto request = ClienteRequestDto.builder()
                .nombre("Lucas")
                .email("repetido@gmail.com")
                .telefono("388123456")
                .build();

        when(clienteRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(NegocioException.class, () -> clienteService.registrar(request));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe listar únicamente los clientes que no estén en estado ELIMINADO")
    void listarTodos_ExcluyeEliminados() {
        Cliente clienteActivo = Cliente.builder()
                .id(UUID.randomUUID())
                .nombre("Activo")
                .email("activo@gmail.com")
                .estado(EstadoCliente.ACTIVO)
                .build();

        when(clienteRepository.findByEstadoNot(EstadoCliente.ELIMINADO)).thenReturn(List.of(clienteActivo));

        List<ClienteResponseDto> lista = clienteService.listarTodos();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(EstadoCliente.ACTIVO, lista.get(0).getEstado());
        verify(clienteRepository).findByEstadoNot(EstadoCliente.ELIMINADO);
    }

    @Test
    @DisplayName("Debe aplicar soft delete cambiando el estado a ELIMINADO cuando es ADMIN")
    void eliminar_ComoAdmin_Exito() {
        UUID id = UUID.randomUUID();
        Cliente cliente = Cliente.builder()
                .id(id)
                .nombre("Por Borrar")
                .estado(EstadoCliente.ACTIVO)
                .build();

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        clienteService.eliminar(id, 1L, "ADMIN");

        assertEquals(EstadoCliente.ELIMINADO, cliente.getEstado());
        verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("Debe aplicar soft delete si el cliente elimina su propia cuenta")
    void eliminar_PropioCliente_Exito() {
        UUID id = UUID.randomUUID();
        Long userId = 4L;
        Cliente cliente = Cliente.builder()
                .id(id)
                .userId(userId)
                .nombre("Cliente Titular")
                .estado(EstadoCliente.ACTIVO)
                .build();

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        clienteService.eliminar(id, userId, "CLIENTE");

        assertEquals(EstadoCliente.ELIMINADO, cliente.getEstado());
        verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("Debe lanzar NegocioException si un cliente intenta eliminar una cuenta ajena")
    void eliminar_ClienteAjeno_LanzaExcepcion() {
        UUID id = UUID.randomUUID();
        Cliente cliente = Cliente.builder()
                .id(id)
                .userId(10L)
                .nombre("Cliente Ajeno")
                .estado(EstadoCliente.ACTIVO)
                .build();

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        assertThrows(NegocioException.class, () -> clienteService.eliminar(id, 4L, "CLIENTE"));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el ID no existe al eliminar")
    void eliminar_IdInexistente_LanzaExcepcion() {
        UUID idInexistente = UUID.randomUUID();
        when(clienteRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> clienteService.eliminar(idInexistente, 1L, "ADMIN"));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el ID no existe al buscar")
    void obtenerPorId_IdInexistente_LanzaExcepcion() {
        UUID idInexistente = UUID.randomUUID();
        when(clienteRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> clienteService.obtenerPorId(idInexistente));
    }
}