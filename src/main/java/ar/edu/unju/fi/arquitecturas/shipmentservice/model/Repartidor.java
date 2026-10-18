package ar.edu.unju.fi.arquitecturas.shipmentservice.model;

import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoRepartidor;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "repartidores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Repartidor extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "tipo_vehiculo", nullable = false)
    private String tipoVehiculo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRepartidor estado;
}