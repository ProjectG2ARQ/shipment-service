package ar.edu.unju.fi.arquitecturas.shipmentservice.service;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioRequestDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.EnvioResponseDto;
import ar.edu.unju.fi.arquitecturas.shipmentservice.model.enums.EstadoEnvio;

import java.util.List;
import java.util.UUID;

/**
 * Servicio para la gestión del ciclo de vida de los envíos y asignación de logística.
 */
public interface EnvioService {

    /**
     * Registra un nuevo envío calculando su tarifa y asignando estado PENDIENTE.
     *
     * @param requestDto Datos del paquete y direcciones.
     * @return Envío persistido.
     */
    EnvioResponseDto crearEnvio(EnvioRequestDto requestDto);

    /**
     * Consulta un envío por su clave primaria.
     *
     * @param id Identificador UUID.
     * @return DTO del envío encontrado.
     */
    EnvioResponseDto obtenerPorId(UUID id);

    /**
     * Busca un envío mediante su código único de tracking.
     *
     * @param codigoSeguimiento Código de seguimiento.
     * @return DTO del envío correspondiente.
     */
    EnvioResponseDto obtenerPorCodigoSeguimiento(String codigoSeguimiento);

    /**
     * Filtra envíos según su estado actual.
     *
     * @param estado Estado de búsqueda.
     * @return Lista de envíos encontrados.
     */
    List<EnvioResponseDto> listarPorEstado(EstadoEnvio estado);

    /**
     * Filtra envíos según su ciudad de destino.
     *
     * @param ciudad Nombre de la ciudad.
     * @return Lista de envíos con destino a la localidad.
     */
    List<EnvioResponseDto> listarPorCiudadDestino(String ciudad);

    /**
     * Transiciona el estado del envío validando las reglas de flujo.
     *
     * @param id Identificador UUID del envío.
     * @param nuevoEstado Estado al que se desea mover.
     * @return Envío actualizado.
     */
    EnvioResponseDto actualizarEstado(UUID id, EstadoEnvio nuevoEstado);

    /**
     * Evalúa si el envío puede ser cancelado.
     *
     * @param id Identificador UUID.
     * @return true si se encuentra en estado PENDIENTE.
     */
    boolean puedeCancelarse(UUID id);

    /**
     * Cancela un envío si cumple las condiciones del negocio.
     *
     * @param id Identificador UUID del envío a cancelar.
     */
    void cancelarEnvio(UUID id);

    /**
     * Asigna un repartidor en estado ACTIVO a un paquete PENDIENTE.
     *
     * @param envioId Identificador UUID del envío.
     * @param repartidorId Identificador UUID del repartidor.
     * @return Envío actualizado con el repartidor asignado.
     */
    EnvioResponseDto asignarRepartidorAEnvio(UUID envioId, UUID repartidorId);
}