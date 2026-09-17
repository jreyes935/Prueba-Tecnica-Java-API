package com.josereyes.payments.service;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.josereyes.payments.dto.PaymentResponse;
import com.josereyes.payments.dto.UpdatePaymentStatusRequest;
import com.josereyes.payments.entity.Payment;
import com.josereyes.payments.entity.PaymentStatus;
import com.josereyes.payments.exception.InvalidPaymentStatusTransitionException;
import com.josereyes.payments.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest{

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldApprovePendingPayment(){             // PENDING -> APPROVED
        Payment payment = new Payment();
        payment.setId("PAY-001");
        payment.setAmount(new BigDecimal("150.50"));
        payment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findById("PAY-001")).thenReturn(Optional.of(payment));

        when(paymentRepository.save(payment)).thenReturn(payment);

        UpdatePaymentStatusRequest request = new UpdatePaymentStatusRequest(PaymentStatus.APPROVED);

        PaymentResponse response = paymentService.updatePaymentStatus("PAY-001", request);          //Ejecuta el cambio de estado

        assertEquals(PaymentStatus.APPROVED, response.status());
        assertEquals(PaymentStatus.APPROVED, payment.getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void shouldRejectApprovedToDeclinedTransition(){    // APPROVED -> DECLINED
        Payment payment = new Payment();
        payment.setId("PAY-002");
        payment.setStatus(PaymentStatus.APPROVED);

        when(paymentRepository.findById("PAY-002")).thenReturn(Optional.of(payment));

        UpdatePaymentStatusRequest request = new UpdatePaymentStatusRequest(PaymentStatus.DECLINED);

        assertThrows(InvalidPaymentStatusTransitionException.class, () -> paymentService.updatePaymentStatus("PAY-002", request));

        assertEquals(PaymentStatus.APPROVED, payment.getStatus());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void shouldDeclinedPendingPayment(){            // PENDING -> DECLINED
        Payment payment = new Payment();
        payment.setId("PAY-003");
        payment.setAmount(new BigDecimal("150.50"));
        payment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findById("PAY-003")).thenReturn(Optional.of(payment));

        when(paymentRepository.save(payment)).thenReturn(payment);

        UpdatePaymentStatusRequest request = new UpdatePaymentStatusRequest(PaymentStatus.DECLINED);

        PaymentResponse response = paymentService.updatePaymentStatus("PAY-003", request);

        assertEquals(PaymentStatus.DECLINED, response.status());
        assertEquals(PaymentStatus.DECLINED, payment.getStatus());

        verify(paymentRepository).save(payment);
    }
}