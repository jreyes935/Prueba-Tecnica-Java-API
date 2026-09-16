package com.josereyes.payments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.josereyes.payments.entity.PaymentStatus;

@ResponseStatus(HttpStatus.CONFLICT)                        //hace que spring responda con 409
public class InvalidPaymentStatusTransitionException extends RuntimeException{
    public InvalidPaymentStatusTransitionException(PaymentStatus currentStatus, PaymentStatus requestedStatus){
        super("No se permite cambiar de " +currentStatus + " a " + requestedStatus);
        }
}