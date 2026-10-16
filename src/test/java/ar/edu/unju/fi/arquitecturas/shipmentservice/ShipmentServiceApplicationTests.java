package ar.edu.unju.fi.arquitecturas.shipmentservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ShipmentServiceApplication.class)
@ActiveProfiles("test")
class ShipmentServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}