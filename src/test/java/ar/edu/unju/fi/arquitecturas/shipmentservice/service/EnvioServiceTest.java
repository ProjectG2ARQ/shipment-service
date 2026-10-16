package ar.edu.unju.fi.arquitecturas.shipmentservice.service;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.DireccionDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.CodigoSeguimientoDuplicadoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.EnvioNoEncontradoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.EstadoEnvioInvalidoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Direccion;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Envio;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoEnvio;
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

    @InjectMocks
    private EnvioServiceImpl envioService;

    private Envio envioEjemplo;
    private EnvioRequestDto requestDto;
    private UUID envioId;

    @BeforeEach
    void setUp() {
        envioId = UUID.randomUUID();

        Direccion direccion = new Direccion("Av. Siempre Viva", "742", "San Salvador de Jujuy", "4600");
        envioEjemplo = new Envio();
        envioEjemplo.setId(envioId);
        envioEjemplo.setCodigoSeguimiento("TRACK-12345");
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
                .codigoSeguimiento("TRACK-12345")
                .peso(2.0)
                .ancho(10.0)
                .alto(15.0)
                .largo(20.0)
                .origen(direccionDto)
                .destino(direccionDto)
                .build();
    }

    @Test
    @DisplayName("Debe crear un envío calculando la tarifa correctamente ($1500 base + $250 por kilo)")
    void crearEnvio_Exito() {
        when(envioRepository.findByCodigoSeguimiento("TRACK-12345")).thenReturn(Optional.empty());
        when(envioRepository.save(any(Envio.class))).thenAnswer(invocation -> {
            Envio e = invocation.getArgument(0);
            e.setId(envioId);
            return e;
        });

        EnvioResponseDto resultado = envioService.crearEnvio(requestDto);

        assertNotNull(resultado);
        assertEquals(envioId, resultado.getId());
        assertEquals(EstadoEnvio.PENDIENTE, resultado.getEstado());
        assertEquals(2000.0, resultado.getTarifa()); // 1500.0 + (2.0 * 250.0)
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar crear un envío con código de seguimiento duplicado")
    void crearEnvio_CodigoDuplicado_LanzaExcepcion() {
        when(envioRepository.findByCodigoSeguimiento("TRACK-12345")).thenReturn(Optional.of(envioEjemplo));

        assertThrows(CodigoSeguimientoDuplicadoException.class, () -> {
            envioService.crearEnvio(requestDto);
        });

        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe obtener un envío por su ID correctamente")
    void obtenerPorId_Exito() {
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));

        EnvioResponseDto resultado = envioService.obtenerPorId(envioId);

        assertNotNull(resultado);
        assertEquals(envioId, resultado.getId());
        assertEquals("TRACK-12345", resultado.getCodigoSeguimiento());
        verify(envioRepository, times(1)).findById(envioId);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el ID no existe")
    void obtenerPorId_NoExiste_LanzaExcepcion() {
        when(envioRepository.findById(envioId)).thenReturn(Optional.empty());

        assertThrows(EnvioNoEncontradoException.class, () -> envioService.obtenerPorId(envioId));
        verify(envioRepository, times(1)).findById(envioId);
    }

    @Test
    @DisplayName("Debe retornar true en puedeCancelarse cuando el estado es PENDIENTE")
    void puedeCancelarse_RetornaTrue() {
        envioEjemplo.setEstado(EstadoEnvio.PENDIENTE);
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));

        boolean resultado = envioService.puedeCancelarse(envioId);

        assertTrue(resultado);
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
    @DisplayName("Debe lanzar excepción al intentar cancelar un envío que ya está EN_RUTA")
    void cancelarEnvio_EstadoInvalido_LanzaExcepcion() {
        envioEjemplo.setEstado(EstadoEnvio.EN_RUTA);
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));

        assertThrows(EstadoEnvioInvalidoException.class, () -> {
            envioService.cancelarEnvio(envioId);
        });

        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe actualizar el estado de PENDIENTE a EN_RUTA exitosamente")
    void actualizarEstado_DePendienteAEnRuta_Exito() {
        envioEjemplo.setEstado(EstadoEnvio.PENDIENTE);
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));
        when(envioRepository.save(any(Envio.class))).thenReturn(envioEjemplo);

        EnvioResponseDto actualizado = envioService.actualizarEstado(envioId, EstadoEnvio.EN_RUTA);

        assertEquals(EstadoEnvio.EN_RUTA, actualizado.getEstado());
        verify(envioRepository, times(1)).save(envioEjemplo);
    }

    @Test
    @DisplayName("Debe permitir transición de EN_RUTA a ENTREGADO")
    void actualizarEstado_DeEnRutaAEntregado_Exito() {
        envioEjemplo.setEstado(EstadoEnvio.EN_RUTA);
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));
        when(envioRepository.save(any(Envio.class))).thenReturn(envioEjemplo);

        EnvioResponseDto actualizado = envioService.actualizarEstado(envioId, EstadoEnvio.ENTREGADO);

        assertEquals(EstadoEnvio.ENTREGADO, actualizado.getEstado());
        verify(envioRepository, times(1)).save(envioEjemplo);
    }

    @Test
    @DisplayName("Debe rechazar salto inválido de PENDIENTE directo a ENTREGADO")
    void actualizarEstado_SaltoInvalido_LanzaExcepcion() {
        envioEjemplo.setEstado(EstadoEnvio.PENDIENTE);
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));

        assertThrows(EstadoEnvioInvalidoException.class, () -> {
            envioService.actualizarEstado(envioId, EstadoEnvio.ENTREGADO);
        });

        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar actualizar un envío con estado terminal ENTREGADO")
    void actualizarEstado_EstadoTerminal_LanzaExcepcion() {
        envioEjemplo.setEstado(EstadoEnvio.ENTREGADO);
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));

        assertThrows(EstadoEnvioInvalidoException.class, () -> {
            envioService.actualizarEstado(envioId, EstadoEnvio.CANCELADO);
        });

        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe listar envíos filtrados por ciudad destino")
    void listarPorCiudadDestino_Exito() {
        when(envioRepository.findByDestino_Ciudad("San Salvador de Jujuy"))
                .thenReturn(Collections.singletonList(envioEjemplo));

        List<EnvioResponseDto> lista = envioService.listarPorCiudadDestino("San Salvador de Jujuy");

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        verify(envioRepository, times(1)).findByDestino_Ciudad("San Salvador de Jujuy");
    }
}