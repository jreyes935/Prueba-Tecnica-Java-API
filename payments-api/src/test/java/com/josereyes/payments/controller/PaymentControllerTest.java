package com.josereyes.payments.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.josereyes.payments.entity.Payment;
import com.josereyes.payments.entity.PaymentStatus;
import com.josereyes.payments.repository.PaymentRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PaymentControllerTest{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void shouldCreatePayment() throws Exception{            // Crea una orden de pago
        String requestBody = """
            {
                "merchantId": "MERCHANT-001",
                "amount": 150.50,
                "currency": "PAB",
                "description": "Compra de prueba",
                "customerEmail": "customer@example.com"
            }
            """;
        
            MvcResult result = mockMvc.perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.merchantId").value("MERCHANT-001"))
                .andExpect(jsonPath("$.amount").value(150.50))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty()).andReturn();

            JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());

            String id = response.get("id").asText();

            Payment savedPayment = paymentRepository.findById(id).orElseThrow();

            assertEquals(PaymentStatus.PENDING, savedPayment.getStatus());
            assertEquals("MERCHANT-001", savedPayment.getMerchantId());
    }

    @ParameterizedTest                                          //Test de amount <= 0
    @ValueSource(strings = {"0", "-10.50"})
    void shouldRejectNonPositiveAmount(String amount) throws Exception {
        long paymentBefore = paymentRepository.count();

        String requestBody = """
            {
                "merchantId": "MERCHANT-001",
                "amount": %s,
                "currency": "PAB",
                "description": "Prueba de importe invalido",
                "customerEmail": "cutomer@example.com"
            }
            """.formatted(amount);
        
            mockMvc.perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("amount debe ser mayor a 0"))
            .andExpect(jsonPath("$.path").value("/api/payments"));

            assertEquals(paymentBefore, paymentRepository.count());
    }

    @Test
    void shouldReturnNotFoundForMissingPayment() throws Exception{              //Consulta de pago inexistente
        mockMvc.perform(get("/api/payments/{id}", "PAY-NO-EXISTE"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("No existe un pago con el ID: PAY-NO-EXISTE"))
        .andExpect(jsonPath("$.path").value("/api/payments/PAY-NO-EXISTE"));
    }

    @Test                                                                       //Confirma que solo acepta dos decimales
    void shouldRejectAmountWithMoreThanTwoDecimals() throws Exception{
        long paymentsBefore = paymentRepository.count();

        String requestBody = """
                {
                    "merchantId": "MERCHANT-001",
                    "amount": 150.505,
                    "currency": "PAB",
                    "description": "Prueba de precision",
                    "customerEmail": "customer@example.com"
                }
                """;
        
        mockMvc.perform(post("/api/payments").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isBadRequest());

        assertEquals(paymentsBefore, paymentRepository.count());
    }
}