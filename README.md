# 📦 LogiTrack · shipment-service

![Java](https://img.shields.io/badge/Java-25-0F766E?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-4F46E5?style=for-the-badge&logo=springboot&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.4.0-0EA5E9?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit%205-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-C5D539?style=for-the-badge&logo=mock-service-worker&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![License](https://img.shields.io/badge/Licencia-MIT-F59E0B?style=for-the-badge)

Microservicio de **LogiTrack** con **Spring Boot** y **MySQL** que gestiona el ciclo de vida de los envíos, su historial de auditoría y la asignación de repartidores. Realizado en el marco de la cursada de **Desarrollo y Arquitecturas Avanzadas de Software** (Universidad Nacional de Jujuy).

**Integrantes:** Jorge Agustín Armella · Lucas Nahuel Armella · Mauricio Fernando Benitez
 
---

## 🛠️ Tecnologías

| Tecnología                  | Uso |
|-----------------------------|-----|
| Java 25                     | Lenguaje |
| Spring Boot 3.4.3           | Framework base |
| Spring Data JPA / Hibernate | Persistencia |
| MySQL 8 (Docker)            | Base de datos |
| springdoc-openapi           | Documentación de la API (Swagger) |
| JUnit 5 / Mockito           | Pruebas |
| Maven (wrapper)             | Build |

## 🧱 Arquitectura

Arquitectura en capas, con DTOs que desacoplan la API REST de las entidades JPA.

```text
shipment-service/
├── controller/      # Endpoints REST
├── service/         # Lógica de negocio
├── repository/      # Repositorios (JpaRepository)
├── entity/          # Entidades JPA y enumeraciones
├── dto/             # Objetos de transferencia
└── exception/       # Excepciones y manejo global de errores
```

## 🗄️ Modelo de dominio

```mermaid
classDiagram
    class AuditableEntity {
        <<abstract>>
        LocalDateTime fechaCreacion
        LocalDateTime fechaUltimaModificacion
    }
    class Cliente {
        UUID id
        String nombre
        String email
        String telefono
    }
    class Envio {
        UUID id
        String codigoSeguimiento
        Double peso
        Double ancho
        Double alto
        Double largo
        Double tarifa
        EstadoEnvio estado
        calcularTarifa()
        puedeCancelarse()
    }
    class Direccion {
        <<Embeddable>>
        String calle
        String numero
        String ciudad
        String codigoPostal
    }
    class Repartidor {
        UUID id
        String nombre
        String tipoVehiculo
        EstadoRepartidor estado
    }
    class EstadoEnvio {
        <<enumeration>>
        PENDIENTE
        EN_RUTA
        ENTREGADO
        CANCELADO
    }
    class EstadoRepartidor {
        <<enumeration>>
        ACTIVO
        INACTIVO
    }
 
    AuditableEntity <|-- Cliente
    AuditableEntity <|-- Envio
    AuditableEntity <|-- Repartidor
    Cliente "1" --> "0..*" Envio : realiza
    Repartidor "0..1" --> "0..*" Envio : asignado a
    Envio *-- Direccion : origen
    Envio *-- Direccion : destino
    Envio --> EstadoEnvio
    Repartidor --> EstadoRepartidor
```

## 🚀 Puesta en marcha

**Requisitos:** Docker (Maven no hace falta, se usa el wrapper).

Levantar `shipment-service` junto con MySQL con un solo comando:

```bash
docker compose up --build
```

## 🧪 Pruebas

```bash
./mvnw test
```

## 📄 Licencia

Licencia MIT. Ver el archivo [LICENSE](LICENSE).
 