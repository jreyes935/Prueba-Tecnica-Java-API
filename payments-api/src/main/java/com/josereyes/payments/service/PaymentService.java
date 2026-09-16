package com.josereyes.payments.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.josereyes.payments.dto.CreatePaymentRequest;
import com.josereyes.payments.dto.PaymentResponse;
import com.josereyes.payments.dto.UpdatePaymentStatusRequest;
import com.josereyes.payments.entity.Payment;
import com.josereyes.payments.entity.PaymentStatus;
import com.josereyes.payments.exception.InvalidPaymentStatusTransitionException;
import com.josereyes.payments.exception.PaymentNotFoundException;
import com.josereyes.payments.repository.PaymentRepository;

@Service                                                            //permite que spring gestione la clase y le proporcione el repositorio a traves del constructior
public class PaymentService{
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    @Transactional                                                  // hace que la operacion de  base de datos se confirme si termina correctamente o se revierta si occure un error que provoque la reversion
    public PaymentResponse createPayment(CreatePaymentRequest request){
        Payment payment = new Payment();

        payment.setId(UUID.randomUUID().toString());
        payment.setMerchantId(request.merchantId());
        payment.setAmount(request.amount());
        payment.setCurrency(request.currency());
        payment.setDescription(request.description());
        payment.setCustomerEmail(request.customerEmail());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        return new PaymentResponse(savedPayment.getId(), savedPayment.getMerchantId(),
         savedPayment.getAmount(), savedPayment.getCurrency(), savedPayment.getDescription(), 
         savedPayment.getCustomerEmail(), savedPayment.getStatus(), savedPayment.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)                         //readOnly indica que la operacion es de consulta
    public PaymentResponse getPaymentById(String id){
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));

        return new PaymentResponse(payment.getId(), payment.getMerchantId(),
         payment.getAmount(), payment.getCurrency(), payment.getDescription(), 
         payment.getCustomerEmail(), payment.getStatus(), payment.getCreatedAt()
        );
    }

    @Transactional
    public PaymentResponse updatePaymentStatus(String id, UpdatePaymentStatusRequest request){
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));

        PaymentStatus currentStatus = payment.getStatus();
        PaymentStatus requestedStatus = request.status();

        if (currentStatus != PaymentStatus.PENDING || requestedStatus == null || requestedStatus == PaymentStatus.PENDING){
            throw new InvalidPaymentStatusTransitionException(currentStatus,requestedStatus);
        }

        payment.setStatus(requestedStatus);

        Payment savedPayment = paymentRepository.save(payment);

        return new PaymentResponse(savedPayment.getId(), savedPayment.getMerchantId(),
         savedPayment.getAmount(), savedPayment.getCurrency(), savedPayment.getDescription(),
          savedPayment.getCustomerEmail(), savedPayment.getStatus(), savedPayment.getCreatedAt());
    }
}