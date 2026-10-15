package ar.edu.unju.fi.arquitecturas.shipmentservice.model;

import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoEnvio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "envios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "codigo_seguimiento", unique = true, nullable = false)
    private String codigoSeguimiento;

    @Column(name = "peso")
    private Double peso;

    @Column(name = "ancho")
    private Double ancho;

    @Column(name = "alto")
    private Double alto;

    @Column(name = "largo")
    private Double largo;

    @Column(name = "tarifa")
    private Double tarifa;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoEnvio estado;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "calle", column = @Column(name = "origen_calle")),
            @AttributeOverride(name = "numero", column = @Column(name = "origen_numero")),
            @AttributeOverride(name = "ciudad", column = @Column(name = "origen_ciudad")),
            @AttributeOverride(name = "codigoPostal", column = @Column(name = "origen_codigo_postal"))
    })
    private Direccion origen;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "calle", column = @Column(name = "destino_calle")),
            @AttributeOverride(name = "numero", column = @Column(name = "destino_numero")),
            @AttributeOverride(name = "ciudad", column = @Column(name = "destino_ciudad")),
            @AttributeOverride(name = "codigoPostal", column = @Column(name = "destino_codigo_postal"))
    })
    private Direccion destino;

    public Boolean puedeCancelarse() {
        return this.estado == EstadoEnvio.PENDIENTE;
    }

    public Double calcularTarifa() {
        return this.tarifa;
    }
}