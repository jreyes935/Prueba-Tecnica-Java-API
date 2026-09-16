package com.josereyes.payments.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.josereyes.payments.entity.PaymentStatus;

public record PaymentResponse(
    String id,
    String merchantId,
    BigDecimal amount,
    String currency,
    String description,
    String customerEmail,
    PaymentStatus status,
    LocalDateTime createdAt
){
}