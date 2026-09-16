package com.josereyes.payments.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josereyes.payments.dto.CreatePaymentRequest;
import com.josereyes.payments.dto.PaymentResponse;
import com.josereyes.payments.service.PaymentService;

import jakarta.validation.Valid;

@RestController                                                     //hace que la clase reciba peticiones y devuelva datos como JSON
@RequestMapping("/api/payments")                                    // + @PostMapping define POST  /api/payments
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
        @Valid @RequestBody CreatePaymentRequest request){                      //@RequestBody convierte el JSOn recibido en CreatePaymentRequest
        PaymentResponse response = paymentService.createPayment(request);       //@Valid activa las validaciones DTO

        URI location = URI.create("/api/payments/" + response.id());

        return ResponseEntity.created(location).body(response);
    }
}