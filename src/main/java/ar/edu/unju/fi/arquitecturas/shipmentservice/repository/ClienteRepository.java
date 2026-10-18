package ar.edu.unju.fi.arquitecturas.shipmentservice.repository;

import ar.edu.unju.fi.arquitecturas.shipmentservice.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    Optional<Cliente> findByEmail(String email);
}