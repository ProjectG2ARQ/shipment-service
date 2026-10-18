package ar.edu.unju.fi.arquitecturas.shipmentservice.service.impl;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.RepartidorRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.RepartidorResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.NegocioException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Repartidor;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoRepartidor;
import ar.edu.unju.fi.arquitecturas.shipmentservice.repository.RepartidorRepository;
import ar.edu.unju.fi.arquitecturas.shipmentservice.service.RepartidorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RepartidorServiceImpl implements RepartidorService {

    private final RepartidorRepository repartidorRepository;

    @Override
    @Transactional
    public RepartidorResponseDto guardarRepartidor(RepartidorRequestDto requestDto) {
        if (repartidorRepository.existsByNombreIgnoreCase(requestDto.getNombre())) {
            throw new NegocioException("Ya existe un repartidor registrado con el nombre: " + requestDto.getNombre());
        }

        Repartidor repartidor = Repartidor.builder()
                .nombre(requestDto.getNombre())
                .tipoVehiculo(requestDto.getTipoVehiculo())
                .estado(requestDto.getEstado())
                .build();

        return toResponseDto(repartidorRepository.save(repartidor));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepartidorResponseDto> obtenerTodosLosRepartidores() {
        return repartidorRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RepartidorResponseDto buscarRepartidorPorId(UUID id) {
        return toResponseDto(buscarEntidadPorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Repartidor buscarEntidadPorId(UUID id) {
        return repartidorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró ningún repartidor con el ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepartidorResponseDto> findByEstado(EstadoRepartidor estado) {
        return repartidorRepository.findByEstado(estado)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    private RepartidorResponseDto toResponseDto(Repartidor entidad) {
        return RepartidorResponseDto.builder()
                .id(entidad.getId())
                .nombre(entidad.getNombre())
                .tipoVehiculo(entidad.getTipoVehiculo())
                .estado(entidad.getEstado())
                .fechaCreacion(entidad.getFechaCreacion())
                .fechaUltimaModificacion(entidad.getFechaUltimaModificacion())
                .build();
    }
}