package ar.edu.unju.fi.arquitecturas.shipmentservice.service.impl;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Repartidor;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoRepartidor;
import ar.edu.unju.fi.arquitecturas.shipmentservice.repository.RepartidorRepository;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.CodigoSeguimientoDuplicadoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.EnvioNoEncontradoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.EstadoEnvioInvalidoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.mapper.EnvioMapper;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Envio;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoEnvio;
import ar.edu.unju.fi.arquitecturas.shipmentservice.repository.EnvioRepository;
import ar.edu.unju.fi.arquitecturas.shipmentservice.service.EnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository envioRepository;
    //agregue esto (soy jorge)
    private final RepartidorRepository repartidorRepository;

    @Override
    @Transactional
    public EnvioResponseDto crearEnvio(EnvioRequestDto requestDto) {
        if (envioRepository.findByCodigoSeguimiento(requestDto.getCodigoSeguimiento()).isPresent()) {
            throw new CodigoSeguimientoDuplicadoException(requestDto.getCodigoSeguimiento());
        }

        Envio envio = EnvioMapper.toEntity(requestDto);

        // Regla de tarifa: base de $1500 + $250 por kilo
        double peso = (envio.getPeso() != null) ? envio.getPeso() : 1.0;
        envio.setTarifa(1500.0 + (peso * 250.0));
        envio.setEstado(EstadoEnvio.PENDIENTE);

        return EnvioMapper.toResponseDto(envioRepository.save(envio));
    }

    @Override
    @Transactional(readOnly = true)
    public EnvioResponseDto obtenerPorId(UUID id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new EnvioNoEncontradoException(id));
        return EnvioMapper.toResponseDto(envio);
    }

    @Override
    @Transactional(readOnly = true)
    public EnvioResponseDto obtenerPorCodigoSeguimiento(String codigoSeguimiento) {
        Envio envio = envioRepository.findByCodigoSeguimiento(codigoSeguimiento)
                .orElseThrow(() -> new EnvioNoEncontradoException(codigoSeguimiento));
        return EnvioMapper.toResponseDto(envio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvioResponseDto> listarPorEstado(EstadoEnvio estado) {
        return envioRepository.findByEstado(estado).stream()
                .map(EnvioMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvioResponseDto> listarPorCiudadDestino(String ciudad) {
        return envioRepository.findByDestino_Ciudad(ciudad).stream()
                .map(EnvioMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public EnvioResponseDto actualizarEstado(UUID id, EstadoEnvio nuevoEstado) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new EnvioNoEncontradoException(id));

        // Validación estricta del flujo PENDIENTE -> EN_RUTA -> ENTREGADO / CANCELADO
        if (!envio.getEstado().puedeTransicionarA(nuevoEstado)) {
            throw new EstadoEnvioInvalidoException(
                    "No se permite la transición de estado desde " + envio.getEstado() + " hacia " + nuevoEstado
            );
        }

        envio.setEstado(nuevoEstado);
        return EnvioMapper.toResponseDto(envioRepository.save(envio));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean puedeCancelarse(UUID id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new EnvioNoEncontradoException(id));
        return envio.getEstado() == EstadoEnvio.PENDIENTE;
    }

    @Override
    @Transactional
    public void cancelarEnvio(UUID id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new EnvioNoEncontradoException(id));

        if (!puedeCancelarse(id)) {
            throw new EstadoEnvioInvalidoException(
                    "El envío no se puede cancelar en su estado actual: " + envio.getEstado()
            );
        }

        envio.setEstado(EstadoEnvio.CANCELADO);
        envioRepository.save(envio);
    }

    @Override
    @Transactional
    public EnvioResponseDto asignarRepartidorAEnvio(UUID envioId, UUID repartidorId) {
        // 1. Buscar el envío
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new EnvioNoEncontradoException("No se encontró el envío con ID: " + envioId));

        // 2. Buscar el repartidor
        Repartidor repartidor = repartidorRepository.findById(repartidorId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el repartidor con ID: " + repartidorId));

        // 3. Validar que el repartidor esté ACTIVO
        if (repartidor.getEstado() != EstadoRepartidor.ACTIVO) {
            throw new EstadoEnvioInvalidoException("No se puede asignar un repartidor inactivo.");
        }

        // 4. Validar que el envío esté PENDIENTE
        if (envio.getEstado() != EstadoEnvio.PENDIENTE) {
            throw new EstadoEnvioInvalidoException("El envío no se encuentra en estado PENDIENTE para ser asignado.");
        }

        // 5. Asignar y guardar
        envio.setRepartidor(repartidor);
        Envio envioGuardado = envioRepository.save(envio);

        // 6. Retornar mapeado a DTO usando el mapper estático del equipo
        return EnvioMapper.toResponseDto(envioGuardado);
    }
}