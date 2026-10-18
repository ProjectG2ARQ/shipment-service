package ar.edu.unju.fi.arquitecturas.shipmentservice.repository;

import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Envio;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnvioRepository extends JpaRepository<Envio, UUID> {

    List<Envio> findByDestino_Ciudad(String ciudad);

    Optional<Envio> findByCodigoSeguimiento(String codigoSeguimiento);

    List<Envio> findByEstado(EstadoEnvio estado);
}
