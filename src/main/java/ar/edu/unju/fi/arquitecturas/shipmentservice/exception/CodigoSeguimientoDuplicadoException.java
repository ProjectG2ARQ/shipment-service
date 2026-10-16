package ar.edu.unju.fi.arquitecturas.shipmentservice.exception;

public class CodigoSeguimientoDuplicadoException extends RuntimeException {

    public CodigoSeguimientoDuplicadoException(String codigoSeguimiento) {
        super("Ya existe un envío registrado con el código de seguimiento: " + codigoSeguimiento);
    }
}