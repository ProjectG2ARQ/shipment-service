package ar.edu.unju.fi.arquitecturas.shipmentservice.exception;

import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoEnvio;

public class EstadoEnvioInvalidoException extends RuntimeException {

    public EstadoEnvioInvalidoException(String mensaje) {
        super(mensaje);
    }

    public static EstadoEnvioInvalidoException noCancelable(EstadoEnvio estadoActual) {
        return new EstadoEnvioInvalidoException(
                "El envío no se puede cancelar en su estado actual: " + estadoActual
        );
    }

    public static EstadoEnvioInvalidoException estadoTerminal(EstadoEnvio estadoActual) {
        return new EstadoEnvioInvalidoException(
                "No se puede actualizar el estado de un envío ya finalizado (" + estadoActual + ")"
        );
    }
}