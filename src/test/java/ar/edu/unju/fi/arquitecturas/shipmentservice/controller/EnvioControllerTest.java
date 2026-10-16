package ar.edu.unju.fi.arquitecturas.shipmentservice.controller;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.DireccionDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.EnvioNoEncontradoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.EstadoEnvioInvalidoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.GlobalExceptionHandler;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoEnvio;
import ar.edu.unju.fi.arquitecturas.shipmentservice.service.EnvioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EnvioControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EnvioService envioService;

    @InjectMocks
    private EnvioController envioController;

    private UUID idEnvio;
    private EnvioRequestDto requestDto;
    private EnvioResponseDto responseDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(envioController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        idEnvio = UUID.randomUUID();

        DireccionDto direccionDto = DireccionDto.builder()
                .calle("Av. Siempre Viva")
                .numero("742")
                .ciudad("San Salvador de Jujuy")
                .codigoPostal("4600")
                .build();

        requestDto = EnvioRequestDto.builder()
                .codigoSeguimiento("TRK-98765")
                .peso(2.5)
                .ancho(10.0)
                .alto(15.0)
                .largo(20.0)
                .origen(direccionDto)
                .destino(direccionDto)
                .build();

        responseDto = EnvioResponseDto.builder()
                .id(idEnvio)
                .codigoSeguimiento("TRK-98765")
                .peso(2.5)
                .tarifa(2125.0)
                .estado(EstadoEnvio.PENDIENTE)
                .origen(direccionDto)
                .destino(direccionDto)
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/envios - Debe retornar 201 CREATED al registrar un envío válido")
    void crearEnvio_PayloadValido_RetornaCreated() throws Exception {
        when(envioService.crearEnvio(any(EnvioRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(idEnvio.toString()))
                .andExpect(jsonPath("$.codigoSeguimiento").value("TRK-98765"))
                .andExpect(jsonPath("$.tarifa").value(2125.0))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    @DisplayName("POST /api/v1/envios - Debe retornar 400 BAD REQUEST si faltan campos obligatorios (@Valid)")
    void crearEnvio_PayloadInvalido_RetornaBadRequest() throws Exception {
        EnvioRequestDto invalido = EnvioRequestDto.builder().build();

        mockMvc.perform(post("/api/v1/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());

        verify(envioService, never()).crearEnvio(any());
    }

    @Test
    @DisplayName("GET /api/v1/envios/{id} - Debe retornar 200 OK cuando el envío existe")
    void obtenerPorId_Existe_RetornaOk() throws Exception {
        when(envioService.obtenerPorId(idEnvio)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/envios/{id}", idEnvio))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idEnvio.toString()))
                .andExpect(jsonPath("$.codigoSeguimiento").value("TRK-98765"));
    }

    @Test
    @DisplayName("GET /api/v1/envios/{id} - Debe retornar 404 NOT FOUND cuando el envío no existe")
    void obtenerPorId_NoExiste_RetornaNotFound() throws Exception {
        when(envioService.obtenerPorId(idEnvio))
                .thenThrow(new EnvioNoEncontradoException(idEnvio));

        mockMvc.perform(get("/api/v1/envios/{id}", idEnvio))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /api/v1/envios/{id}/estado - Debe retornar 200 OK al actualizar a un estado válido")
    void actualizarEstado_TransicionValida_RetornaOk() throws Exception {
        responseDto.setEstado(EstadoEnvio.EN_RUTA);
        when(envioService.actualizarEstado(eq(idEnvio), eq(EstadoEnvio.EN_RUTA)))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/envios/{id}/estado", idEnvio)
                        .param("nuevoEstado", "EN_RUTA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_RUTA"));
    }

    @Test
    @DisplayName("PATCH /api/v1/envios/{id}/estado - Debe capturar EstadoEnvioInvalidoException")
    void actualizarEstado_TransicionInvalida_RetornaError() throws Exception {
        when(envioService.actualizarEstado(eq(idEnvio), eq(EstadoEnvio.ENTREGADO)))
                .thenThrow(new EstadoEnvioInvalidoException("Transición inválida"));

        mockMvc.perform(patch("/api/v1/envios/{id}/estado", idEnvio)
                        .param("nuevoEstado", "ENTREGADO"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /api/v1/envios/{id}/cancelar - Debe retornar 204 NO CONTENT al cancelar con éxito")
    void cancelarEnvio_Valido_RetornaNoContent() throws Exception {
        doNothing().when(envioService).cancelarEnvio(idEnvio);

        mockMvc.perform(post("/api/v1/envios/{id}/cancelar", idEnvio))
                .andExpect(status().isNoContent());
    }
}