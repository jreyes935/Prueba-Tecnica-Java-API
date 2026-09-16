package com.josereyes.payments.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.josereyes.payments.entity.Payment;
import com.josereyes.payments.entity.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByMerchantId(String merchantId);
    List<Payment> findByMerchantIdAndStatus(String merchantId, PaymentStatus status);
}