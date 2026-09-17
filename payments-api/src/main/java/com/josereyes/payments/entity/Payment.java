package com.josereyes.payments.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity                                 //indica que JPA guardara objetos de esta clase en la base de datos
@Table(name = "payments")               //establece el nombre de la tabla
public class Payment {
    @Id                                 //Identifica la clave primaria de cada pago
    private String id;
    private String merchantId;
    @Column(nullable = false, precision = 19, scale = 2)        //position=19 ndica cantidad de digitos y scale=2 indica cuantos puestos decimales puede tener
    private BigDecimal amount;
    private String currency;
    private String description;
    private String customerEmail;

    @Enumerated(EnumType.STRING)        //Guarda el nombre del estado
    private PaymentStatus status;

    private LocalDateTime createdAt;

    public Payment(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}