package com.prueba.credit_application_service.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenApiConfig - NUEVO según requerimientos del enunciado
 * Configuración de Swagger/OpenAPI para documentación de la API
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:credit-application-service}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        // Define el esquema de seguridad JWT
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("CoopCredit - Credit Application API")
                        .description("""
                                Sistema Integral de Solicitudes de Crédito con Arquitectura Hexagonal.
                                
                                ## Funcionalidades principales:
                                - Gestión de afiliados
                                - Solicitudes de crédito
                                - Evaluación automática de riesgo
                                - Autenticación con JWT
                                - Control de acceso por roles
                                
                                ## Roles disponibles:
                                - **ROLE_AFILIADO**: Puede crear y ver sus propias solicitudes
                                - **ROLE_ANALISTA**: Puede ver todas las solicitudes y aprobar/rechazar
                                - **ROLE_ADMIN**: Acceso completo al sistema
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("CoopCredit Development Team")
                                .email("dev@coopcredit.com")
                                .url("https://coopcredit.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Docker Environment")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingrese el token JWT obtenido del endpoint /auth/login")));
    }
}