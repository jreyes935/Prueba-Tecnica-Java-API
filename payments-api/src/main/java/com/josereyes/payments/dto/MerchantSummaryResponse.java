package com.josereyes.payments.dto;

import java.math.BigDecimal;

public record MerchantSummaryResponse(
    String merchantId,
    long totalPayments,
    long approvedPayments,
    long declinedPayments,
    long cancelledPayments,
    BigDecimal totalApprovedAmount
){
}