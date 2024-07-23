package com.tada.darajab2c.service;


import com.tada.darajab2c.dto.*;
import com.tada.darajab2c.entity.PaymentStatusEntity;
import com.tada.darajab2c.repository.B2CRequestRepository;
import com.tada.darajab2c.repository.PaymentStatusRepository;
import com.tada.darajab2c.util.OAuthTokenGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class B2CServiceIntegrationTest {

    @Autowired
    private B2CService b2cService;

    @MockBean
    private B2CRequestRepository b2cRequestRepository;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private PaymentStatusRepository paymentStatusRepository;

    @MockBean
    private KafkaTemplate<String, B2CRequest> kafkaTemplate;

    @MockBean
    private OAuthTokenGenerator oAuthTokenGenerator;


    @Test
    public void testUpdatePaymentStatus() {
        UUID id = UUID.randomUUID();
        PaymentStatusEntity paymentStatus = PaymentStatusEntity.builder()
                .transactionId(id)
                .status("Pending")
                .build();


        PaymentStatusUpdate update = new PaymentStatusUpdate();
        update.setTransactionID(id.toString());
        update.setStatus("Completed");
        update.setDetails("Details updated");

        when(paymentStatusRepository.findById(
                id.toString()
        )).thenReturn(Optional.of(paymentStatus));

        b2cService.updatePaymentStatus(update);

        verify(paymentStatusRepository, times(1)).save(paymentStatus);
        assertEquals("Completed", paymentStatus.getStatus());
        assertEquals("Details updated", paymentStatus.getDetails());
    }
}
