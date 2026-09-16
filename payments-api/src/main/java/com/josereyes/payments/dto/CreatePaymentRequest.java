package com.josereyes.payments.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(
    @NotBlank(message = "merchantID es requerido")
    String merchantId,

    @NotNull(message = "amount es requerido")
    @DecimalMin(
        value = "0",
        inclusive = false,
        message = "amount debe ser mayor a 0"
    )
    BigDecimal amount,

    @NotBlank(message = "currency es requerido")
    String currency,

    @NotBlank(message = "description es requerido")
    String description,

    @NotBlank(message = "customerEmail es requerido")
    @Email(message = "cutomerEmail debe tener un formato valido")
    String customerEmail
){
}