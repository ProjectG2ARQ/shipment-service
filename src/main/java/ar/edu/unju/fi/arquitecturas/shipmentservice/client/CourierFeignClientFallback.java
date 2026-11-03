package ar.edu.unju.fi.arquitecturas.shipmentservice.client;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.RepartidorCourierDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CourierFeignClientFallback implements CourierFeignClient {

    @Override
    public RepartidorCourierDto obtenerRepartidorPorId(UUID id, String token) {
        return RepartidorCourierDto.builder()
                .estado("INDISPONIBLE")
                .build();
    }
}