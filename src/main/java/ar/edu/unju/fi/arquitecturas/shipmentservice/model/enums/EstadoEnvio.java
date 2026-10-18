package ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums;

public enum EstadoEnvio {
    PENDIENTE,
    EN_RUTA,
    ENTREGADO,
    CANCELADO;

    public boolean puedeTransicionarA(EstadoEnvio nuevoEstado) {
        return switch (this) {
            case PENDIENTE -> nuevoEstado == EN_RUTA || nuevoEstado == CANCELADO;
            case EN_RUTA -> nuevoEstado == ENTREGADO;
            case ENTREGADO, CANCELADO -> false; // Estados finales, no admiten más cambios
        };
    }
}
