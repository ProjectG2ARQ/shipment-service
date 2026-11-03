package ar.edu.unju.fi.arquitecturas.shipmentservice.service;

import ar.edu.unju.fi.arquitecturas.shipmentservice.client.CourierFeignClient;
import jakarta.servlet.http.HttpServletRequest;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.DireccionDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.EnvioNoEncontradoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.EstadoEnvioInvalidoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Cliente;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Direccion;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Envio;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoEnvio;
import ar.edu.unju.fi.arquitecturas.shipmentservice.repository.ClienteRepository;
import ar.edu.unju.fi.arquitecturas.shipmentservice.repository.EnvioRepository;
import ar.edu.unju.fi.arquitecturas.shipmentservice.service.impl.EnvioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private CourierFeignClient courierFeignClient;

    @InjectMocks
    private EnvioServiceImpl envioService;

    private Envio envioEjemplo;
    private EnvioRequestDto requestDto;
    private UUID envioId;
    private UUID clienteId;
    private Cliente clienteEjemplo;

    @BeforeEach
    void setUp() {
        envioId = UUID.randomUUID();
        clienteId = UUID.randomUUID();

        clienteEjemplo = new Cliente();
        clienteEjemplo.setId(clienteId);
        clienteEjemplo.setNombre("Juan Perez");
        clienteEjemplo.setEmail("juan@logitrack.com");

        Direccion direccion = new Direccion("Av. Siempre Viva", "742", "San Salvador de Jujuy", "4600");

        envioEjemplo = new Envio();
        envioEjemplo.setId(envioId);
        envioEjemplo.setCodigoSeguimiento("TRK-12345678");
        envioEjemplo.setCliente(clienteEjemplo);
        envioEjemplo.setPeso(2.0);
        envioEjemplo.setAncho(10.0);
        envioEjemplo.setAlto(15.0);
        envioEjemplo.setLargo(20.0);
        envioEjemplo.setTarifa(2000.0);
        envioEjemplo.setEstado(EstadoEnvio.PENDIENTE);
        envioEjemplo.setOrigen(direccion);
        envioEjemplo.setDestino(direccion);

        DireccionDto direccionDto = DireccionDto.builder()
                .calle("Av. Siempre Viva")
                .numero("742")
                .ciudad("San Salvador de Jujuy")
                .codigoPostal("4600")
                .build();

        requestDto = EnvioRequestDto.builder()
                .clienteId(clienteId)
                .peso(2.0)
                .ancho(10.0)
                .alto(15.0)
                .largo(20.0)
                .origen(direccionDto)
                .destino(direccionDto)
                .build();
    }

    @Test
    @DisplayName("Debe crear un envío exitosamente generando el código automático y calculando tarifa")
    void crearEnvio_Exito() {
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteEjemplo));
        when(envioRepository.findByCodigoSeguimiento(anyString())).thenReturn(Optional.empty());
        when(envioRepository.save(any(Envio.class))).thenAnswer(invocation -> {
            Envio e = invocation.getArgument(0);
            e.setId(envioId);
            return e;
        });

        EnvioResponseDto resultado = envioService.crearEnvio(requestDto);

        assertNotNull(resultado);
        assertEquals(envioId, resultado.getId());
        assertEquals(EstadoEnvio.PENDIENTE, resultado.getEstado());
        assertNotNull(resultado.getCodigoSeguimiento());
        assertEquals(2000.0, resultado.getTarifa()); // 1500.0 + (2.0 * 250.0)
        verify(clienteRepository, times(1)).findById(clienteId);
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe obtener un envío por su ID correctamente")
    void obtenerPorId_Exito() {
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));

        EnvioResponseDto resultado = envioService.obtenerPorId(envioId);

        assertNotNull(resultado);
        assertEquals(envioId, resultado.getId());
        assertEquals("TRK-12345678", resultado.getCodigoSeguimiento());
        verify(envioRepository, times(1)).findById(envioId);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el ID no existe al buscar")
    void obtenerPorId_NoExiste_LanzaExcepcion() {
        when(envioRepository.findById(envioId)).thenReturn(Optional.empty());

        assertThrows(EnvioNoEncontradoException.class, () -> envioService.obtenerPorId(envioId));
        verify(envioRepository, times(1)).findById(envioId);
    }

    @Test
    @DisplayName("Debe cancelar el envío exitosamente si está en estado PENDIENTE")
    void cancelarEnvio_EstadoPendiente_Exito() {
        envioEjemplo.setEstado(EstadoEnvio.PENDIENTE);
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));
        when(envioRepository.save(any(Envio.class))).thenReturn(envioEjemplo);

        envioService.cancelarEnvio(envioId);

        assertEquals(EstadoEnvio.CANCELADO, envioEjemplo.getEstado());
        verify(envioRepository, times(1)).save(envioEjemplo);
    }

    @Test
    @DisplayName("Debe actualizar el estado de PENDIENTE a EN_RUTA exitosamente")
    void actualizarEstado_DePendienteAEnRuta_Exito() {
        envioEjemplo.setEstado(EstadoEnvio.PENDIENTE);
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));
        when(envioRepository.save(any(Envio.class))).thenReturn(envioEjemplo);

        EnvioResponseDto actualizado = envioService.actualizarEstado(envioId, EstadoEnvio.EN_RUTA);

        assertEquals(EstadoEnvio.EN_RUTA, actualizado.getEstado());
        verify(envioRepository, times(1)).save(any(Envio.class)); // <-- Corregido aquí
    }
}