package ar.edu.unju.fi.arquitecturas.shipmentservice.exception;

import java.util.UUID;

public class EnvioNoEncontradoException extends RuntimeException {

    public EnvioNoEncontradoException(UUID id) {
        super("No se encontró el envío con ID: " + id);
    }

    public EnvioNoEncontradoException(String codigoSeguimiento) {
        super("No se encontró el envío con código de seguimiento: " + codigoSeguimiento);
    }
}