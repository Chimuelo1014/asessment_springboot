package com.prueba.credit_application_service.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;

    // ⚠️ Este campo NO debe ser usado en producción
    // Solo para testing. En producción, forzar a ROLE_AFILIADO
    private String role;
}
