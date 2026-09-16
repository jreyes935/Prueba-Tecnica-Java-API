package com.josereyes.payments.dto;

import com.josereyes.payments.entity.PaymentStatus;

import jakarta.validation.constraints.NotNull;

public record UpdatePaymentStatusRequest(
    @NotNull(message = "status es requerido")           //NotNull exige que se envie un estado
    PaymentStatus status
){
}