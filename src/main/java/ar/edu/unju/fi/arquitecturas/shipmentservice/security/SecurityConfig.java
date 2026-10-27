package ar.edu.unju.fi.arquitecturas.shipmentservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/shipments", "/api/v1/envios").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/shipments/*/assign/**", "/api/v1/envios/*/asignar/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/shipments/*/assign/**", "/api/v1/envios/*/asignar/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/shipments/*/status/**", "/api/v1/envios/*/estado/**").hasRole("REPARTIDOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/shipments/*/status/**", "/api/v1/envios/*/estado/**").hasRole("REPARTIDOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}