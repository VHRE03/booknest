package com.vhre.booknest.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI / Swagger documentation configuration.
 * The server URL is externalized so each environment (local, dev, prod) can
 * point the "Try it out" requests to the right entry point.
 */
@Configuration
public class OpenApiConfig {

    @Value("${openapi.server.url:http://localhost:8080}")
    private String serverUrl;

    @Value("${openapi.server.description:Servidor Local}")
    private String serverDescription;

    @Bean
    public OpenAPI bookNestOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BookNest API")
                        .description("API REST para la gestión de una librería: autores, libros y reseñas. "
                                + "Proyecto standalone (sin dependencia del paquete base) que mantiene la "
                                + "arquitectura por módulos: entity, repository, mapper, service, "
                                + "serviceImpl, DTO y controller.")
                        .version("v1.0.0")
                        .contact(new Contact().name("Equipo de Arquitectura").email("arch@booknest.com"))
                        .license(new License().name("Propiedad de BookNest").url("https://booknest.com")))
                .servers(List.of(new Server().url(serverUrl).description(serverDescription)))
                .components(new Components());
    }
}
