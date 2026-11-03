package ar.edu.unju.fi.arquitecturas.shipmentservice.client;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.RepartidorCourierDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CourierFeignClientFallback implements CourierFeignClient {

    @Override
    public RepartidorCourierDto obtenerRepartidorPorId(UUID id, String token) {
        // En caso de caída de red, timeout o error 500 en courier-service,
        // retorna un objeto seguro indicando que el servicio no está disponible
        return RepartidorCourierDto.builder()
                .estado("INDISPONIBLE")
                .build();
    }
}