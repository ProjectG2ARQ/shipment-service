package ar.edu.unju.fi.arquitecturas.shipmentservice.model;

import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoEnvio;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "envios")
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    String codigoSeguimiento;
    Double peso;
    Double ancho;
    Double alto;
    Double largo;
    Double tarifa;
    EstadoEnvio estado;
}
