package ar.edu.unju.fi.arquitecturas.shipmentservice.service.impl;
import ar.edu.unju.fi.arquitecturas.shipmentservice.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Repartidor;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoRepartidor;
import ar.edu.unju.fi.arquitecturas.shipmentservice.repository.RepartidorRepository;
import ar.edu.unju.fi.arquitecturas.shipmentservice.service.RepartidorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RepartidorServiceImpl implements RepartidorService {

    private final RepartidorRepository repartidorRepository;

    @Override
    public Repartidor guardarRepartidor(Repartidor repartidor) {
        return repartidorRepository.save(repartidor);
    }

    @Override
    public List<Repartidor> obtenerTodosLosRepartidores() {
        return repartidorRepository.findAll();
    }

    @Override
    public Repartidor buscarRepartidorPorId(UUID id) {
        return repartidorRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró ningún repartidor con el ID: " + id));
    }

    @Override
    public List<Repartidor> findByEstado(EstadoRepartidor estado) {
        return repartidorRepository.findByEstado(estado);
    }
}