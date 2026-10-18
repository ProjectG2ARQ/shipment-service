package ar.edu.unju.fi.arquitecturas.shipmentservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LogiTrack - shipment-service API")
                        .version("1.0.0")
                        .description("Microservicio de gestión de envíos, asignación de repartidores y clientes.")
                        .contact(new Contact().name("Grupo 2 ARQ")));
    }
}