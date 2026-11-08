package ar.edu.unju.fi.arquitecturas.shipmentservice.service.impl;

import ar.edu.unju.fi.arquitecturas.shipmentservice.client.CourierFeignClient;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.RepartidorCourierDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.*;
import ar.edu.unju.fi.arquitecturas.shipmentservice.mapper.EnvioMapper;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Cliente;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Envio;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoEnvio;
import ar.edu.unju.fi.arquitecturas.shipmentservice.repository.ClienteRepository;
import ar.edu.unju.fi.arquitecturas.shipmentservice.repository.EnvioRepository;
import ar.edu.unju.fi.arquitecturas.shipmentservice.service.EnvioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnvioServiceImpl implements EnvioService {

    private final CourierFeignClient courierFeignClient;
    private final EnvioRepository envioRepository;
    private final ClienteRepository clienteRepository;
    private final HttpServletRequest request;

    @Override
    @Transactional
    public EnvioResponseDto crearEnvio(EnvioRequestDto requestDto) {
        Cliente cliente = clienteRepository.findById(requestDto.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el cliente con ID: " + requestDto.getClienteId()));

        Envio envio = EnvioMapper.toEntity(requestDto);
        envio.setCliente(cliente);

        String codigoGenerado;
        do {
            codigoGenerado = "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (envioRepository.findByCodigoSeguimiento(codigoGenerado).isPresent());

        envio.setCodigoSeguimiento(codigoGenerado);

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
        String userIdHeader = request.getHeader("X-Auth-User-Id");
        String userRoleHeader = request.getHeader("X-Auth-User-Role");

        if (userIdHeader != null && !"ADMIN".equalsIgnoreCase(userRoleHeader)) {
            Long usuarioAutenticadoId = Long.parseLong(userIdHeader);
            if (envio.getCliente() == null || !usuarioAutenticadoId.equals(envio.getCliente().getUserId())) {
                throw new NegocioException("Acceso denegado: este paquete no pertenece a su cuenta.");
            }
        }

        envio.setEstado(EstadoEnvio.CANCELADO);
        envioRepository.save(envio);
    }

    @Override
    @Transactional
    public EnvioResponseDto asignarRepartidorAEnvio(UUID envioId, UUID repartidorId) {
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new EnvioNoEncontradoException(envioId));

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            RepartidorCourierDto repartidor = courierFeignClient.obtenerRepartidorPorId(repartidorId, authHeader);

            if (repartidor == null || "INACTIVO".equals(repartidor.getEstado())) {
                throw new NegocioException("No se puede asignar: el repartidor se encuentra INACTIVO.");
            }

        } catch (feign.FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("No se encontró ningún repartidor con el ID: " + repartidorId);
        } catch (feign.FeignException.Forbidden | feign.FeignException.Unauthorized e) {
            throw new NegocioException("Error de permisos al consultar el repartidor. Verificá el Token.");
        }

        envio.setRepartidorId(repartidorId);
        return EnvioMapper.toResponseDto(envioRepository.save(envio));
    }
    @Override
    @Transactional
    public void cancelarEnvioPorCliente(String codigoSeguimiento, Long userId) {

        Cliente clienteAutenticado = clienteRepository.findByUserId(userId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el perfil del cliente solicitante."));
        Envio envio = envioRepository.findByCodigoSeguimiento(codigoSeguimiento)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un envío con el código: " + codigoSeguimiento));
        if (!envio.getCliente().getId().equals(clienteAutenticado.getId())) {
            throw new NegocioException("Acceso denegado: este envío pertenece a otro cliente.");
        }
        if (envio.getEstado() != EstadoEnvio.PENDIENTE) {
            throw new NegocioException("No se puede cancelar un envío que ya está en tránsito o entregado.");
        }
        envio.setEstado(EstadoEnvio.CANCELADO);
        envioRepository.save(envio);
    }

}