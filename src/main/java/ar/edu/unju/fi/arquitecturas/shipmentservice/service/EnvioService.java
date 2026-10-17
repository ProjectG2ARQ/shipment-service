package ar.edu.unju.fi.arquitecturas.shipmentservice.service;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Envio;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoEnvio;

import java.util.List;
import java.util.UUID;

public interface EnvioService {

    EnvioResponseDto crearEnvio(EnvioRequestDto requestDto);

    EnvioResponseDto obtenerPorId(UUID id);

    EnvioResponseDto obtenerPorCodigoSeguimiento(String codigoSeguimiento);

    List<EnvioResponseDto> listarPorEstado(EstadoEnvio estado);

    List<EnvioResponseDto> listarPorCiudadDestino(String ciudad);

    EnvioResponseDto actualizarEstado(UUID id, EstadoEnvio nuevoEstado);

    boolean puedeCancelarse(UUID id);

    void cancelarEnvio(UUID id);
    //yo agregue esto soy jorge
    EnvioResponseDto asignarRepartidorAEnvio(UUID envioId, UUID repartidorId);
}