# ==========================================
# Etapa 1: Build y empaquetado
# ==========================================
FROM eclipse-temurin:25-jdk AS builder
WORKDIR /build

# Copiar archivos de Maven Wrapper para aprovechar cache de capas
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copiar el código fuente y compilar omitiendo pruebas para agilizar el build
COPY src/ ./src/
RUN ./mvnw clean package -DskipTests

# ==========================================
# Etapa 2: Imagen final de ejecución (Runtime)
# ==========================================
FROM eclipse-temurin:25-jre-noble
WORKDIR /app

# Crear usuario y grupo no privilegiado por seguridad
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copiar únicamente el archivo jar resultante desde la etapa builder
COPY --from=builder /build/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

# Flag necesario para compatibilidad de bytecode Java 25 con el escáner de Spring
ENTRYPOINT ["java", "-Dspring.classformat.ignore=true", "-jar", "app.jar"]