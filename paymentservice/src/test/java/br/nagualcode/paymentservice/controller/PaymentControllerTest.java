package br.nagualcode.paymentservice.controller;

import br.nagualcode.paymentservice.model.Payment;
import br.nagualcode.paymentservice.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.context.ActiveProfiles;





@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class PaymentControllerTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentRepository paymentRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    public void setup() {
        paymentRepository.deleteAll();
    }

    @Test
    public void testPaymentStatusUpdate() throws Exception {
        // Simulate updating the payment status to "paid"
        mockMvc.perform(put("/payments/1")
                .param("status", "paid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("paid"));

        // Verify the payment status has been updated
        mockMvc.perform(get("/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("paid"));

        // Change the payment status to "unpaid"
        mockMvc.perform(put("/payments/1")
                .param("status", "unpaid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("unpaid"));
    }
}
