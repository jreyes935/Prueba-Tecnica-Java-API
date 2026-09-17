package com.josereyes.payments.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.josereyes.payments.dto.MerchantSummaryResponse;
import com.josereyes.payments.dto.PaymentResponse;
import com.josereyes.payments.entity.PaymentStatus;
import com.josereyes.payments.service.PaymentService;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {
    private final PaymentService paymentService;

    public MerchantController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @GetMapping("/{merchantId}/payments")
    public ResponseEntity<List<PaymentResponse>> getmerchantPayments(@PathVariable String merchantId, @RequestParam(required = false) PaymentStatus status){                //RequestParam hace que status sea opcional
        List<PaymentResponse> payments = paymentService.getMerchantPayments(merchantId, status);

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{merchantId}/summary")
    public ResponseEntity<MerchantSummaryResponse> getMerchantSummary(@PathVariable String merchantId){
        MerchantSummaryResponse summary = paymentService.getMerchantSummary(merchantId);

        return ResponseEntity.ok(summary);
    }
}