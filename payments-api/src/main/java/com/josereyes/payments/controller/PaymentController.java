package com.josereyes.payments.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josereyes.payments.dto.CreatePaymentRequest;
import com.josereyes.payments.dto.PaymentResponse;
import com.josereyes.payments.dto.UpdatePaymentStatusRequest;
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

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable String id){         //@PathVariable toma la id de la direccion solicitada, si no existe, envia error 404
        PaymentResponse response = paymentService.getPaymentById(id);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")                                                           //Revisa la peticion para modificar el estado
    public ResponseEntity<PaymentResponse> updatePaymentStatus(@PathVariable String id, @Valid @RequestBody UpdatePaymentStatusRequest request){
        PaymentResponse response = paymentService.updatePaymentStatus(id, request);

        return ResponseEntity.ok(response);
    }
}