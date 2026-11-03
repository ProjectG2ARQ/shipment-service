package ar.edu.unju.fi.arquitecturas.shipmentservice.client;

import ar.edu.unju.fi.arquitecturas.shipmentservice.dto.RepartidorCourierDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "courier-service", fallback = CourierFeignClientFallback.class)
public interface CourierFeignClient {

    @GetMapping("/api/v1/repartidores/{id}")
    RepartidorCourierDto obtenerRepartidorPorId(
            @PathVariable("id") UUID id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    );
}