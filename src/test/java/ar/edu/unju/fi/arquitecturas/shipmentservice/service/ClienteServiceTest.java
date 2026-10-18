package ar.edu.unju.fi.arquitecturas.shipmentservice.service;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteRequestDTO;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.ClienteResponseDTO;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.NegocioException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.RecursoNoEncotradoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Cliente;
import ar.edu.unju.fi.arquitecturas.shipmentservice.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
        ClienteRequestDTO request = ClienteRequestDTO.builder()
                .nombre("Lucas")
                .email("lucas@sgmail.com")
                .telefono("388123456")
                .build();

        Cliente entidadGuardada = Cliente.builder()
                .id(UUID.randomUUID())
                .nombre(request.getNombre())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .createdAt(LocalDateTime.now())
                .build();

        when(clienteRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(entidadGuardada);

        ClienteResponseDTO resultado = clienteService.registrar(request);

        assertNotNull(resultado);
        assertEquals(request.getEmail(), resultado.getEmail());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Debe lanzar NegocioException al intentar registrar un email repetido")
    void registrar_EmailExistente_LanzaExcepcion() {
        ClienteRequestDTO request = ClienteRequestDTO.builder()
                .nombre("Lucas")
                .email("repetido@gmail.com")
                .telefono("388123456")
                .build();

        when(clienteRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(NegocioException.class, () -> clienteService.registrar(request));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el ID no existe")
    void obtenerPorId_IdInexistente_LanzaExcepcion() {
        UUID idInexistente = UUID.randomUUID();
        when(clienteRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncotradoException.class, () -> clienteService.obtenerPorId(idInexistente));
    }
}