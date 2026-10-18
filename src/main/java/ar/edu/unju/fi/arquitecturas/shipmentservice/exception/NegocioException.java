package ar.edu.unju.fi.arquitecturas.shipmentservice.exception;

public class NegocioException extends RuntimeException {
    public NegocioException(String message) {
        super(message);
    }
}