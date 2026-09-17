package com.josereyes.payments.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.josereyes.payments.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest{

    @Mock
    private PaymentRepository paymentRipository;

    @InjectMocks
    private PaymentService paymentService;
}