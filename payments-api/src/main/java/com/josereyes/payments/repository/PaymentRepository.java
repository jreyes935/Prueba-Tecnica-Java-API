package com.josereyes.payments.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.josereyes.payments.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    
}