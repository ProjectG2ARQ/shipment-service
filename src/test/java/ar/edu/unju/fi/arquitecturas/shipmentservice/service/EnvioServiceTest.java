package ar.edu.unju.fi.arquitecturas.shipmentservice.service;

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
    private UUID envioId;

    @BeforeEach
    void setUp() {
        envioId = UUID.randomUUID();
        envioEjemplo = new Envio();
        envioEjemplo.setId(envioId);
        envioEjemplo.setCodigoSeguimiento("TRACK-12345");
        envioEjemplo.setPeso(2.0);
        envioEjemplo.setEstado(EstadoEnvio.PENDIENTE);
    }

    @Test
    @DisplayName("Debe crear un envío exitosamente si el código de seguimiento no existe")
    void crearEnvio_Exito() {
        when(envioRepository.findByCodigoSeguimiento("TRACK-12345")).thenReturn(Optional.empty());
        when(envioRepository.save(any(Envio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Envio resultado = envioService.crearEnvio(envioEjemplo);

        assertNotNull(resultado);
        assertEquals(EstadoEnvio.PENDIENTE, resultado.getEstado());
        assertEquals(2000.0, resultado.getTarifa()); // 1500.0 + (2.0 * 250.0)
        verify(envioRepository, times(1)).save(envioEjemplo);
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar crear un envío con código de seguimiento duplicado")
    void crearEnvio_CodigoDuplicado_LanzaExcepcion() {
        when(envioRepository.findByCodigoSeguimiento("TRACK-12345")).thenReturn(Optional.of(envioEjemplo));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            envioService.crearEnvio(envioEjemplo);
        });

        assertTrue(exception.getMessage().contains("Ya existe un envío registrado"));
        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe calcular la tarifa correctamente según el peso")
    void calcularTarifa_CalculoCorrecto() {
        envioEjemplo.setPeso(4.0);
        Double tarifa = envioService.calcularTarifa(envioEjemplo);

        // 1500.0 + (4.0 * 250.0) = 2500.0
        assertEquals(2500.0, tarifa);
    }

    @Test
    @DisplayName("Debe obtener un envío por su ID correctamente")
    void obtenerPorId_Exito() {
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));

        Envio resultado = envioService.obtenerPorId(envioId);

        assertNotNull(resultado);
        assertEquals(envioId, resultado.getId());
        verify(envioRepository, times(1)).findById(envioId);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el ID no existe")
    void obtenerPorId_NoExiste_LanzaExcepcion() {
        when(envioRepository.findById(envioId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> envioService.obtenerPorId(envioId));
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
    @DisplayName("Debe lanzar excepción al intentar cancelar un envío que no está PENDIENTE")
    void cancelarEnvio_EstadoInvalido_LanzaExcepcion() {
        // Usamos EN_RUTA del enum oficial
        envioEjemplo.setEstado(EstadoEnvio.EN_RUTA);
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            envioService.cancelarEnvio(envioId);
        });

        assertTrue(exception.getMessage().contains("no se puede cancelar"));
        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe actualizar el estado a EN_RUTA exitosamente si no es terminal")
    void actualizarEstado_Exito() {
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));
        when(envioRepository.save(any(Envio.class))).thenReturn(envioEjemplo);

        Envio actualizado = envioService.actualizarEstado(envioId, EstadoEnvio.EN_RUTA);

        assertEquals(EstadoEnvio.EN_RUTA, actualizado.getEstado());
        verify(envioRepository, times(1)).save(envioEjemplo);
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar actualizar un envío con estado terminal ENTREGADO")
    void actualizarEstado_EstadoTerminal_LanzaExcepcion() {
        envioEjemplo.setEstado(EstadoEnvio.ENTREGADO);
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioEjemplo));

        assertThrows(RuntimeException.class, () -> {
            envioService.actualizarEstado(envioId, EstadoEnvio.CANCELADO);
        });

        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe listar envíos filtrados por ciudad destino")
    void listarPorCiudadDestino_Exito() {
        when(envioRepository.findByDestino_Ciudad("San Salvador de Jujuy"))
                .thenReturn(Collections.singletonList(envioEjemplo));

        List<Envio> lista = envioService.listarPorCiudadDestino("San Salvador de Jujuy");

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        verify(envioRepository, times(1)).findByDestino_Ciudad("San Salvador de Jujuy");
    }
}