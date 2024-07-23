package com.tada.darajab2c.service;

import static org.junit.jupiter.api.Assertions.*;


import com.tada.darajab2c.dto.*;
import com.tada.darajab2c.entity.PaymentStatusEntity;
import com.tada.darajab2c.exception.BadRequestException;
import com.tada.darajab2c.repository.B2CRequestRepository;
import com.tada.darajab2c.repository.PaymentStatusRepository;
import com.tada.darajab2c.util.OAuthTokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class B2CServiceTest {

    @Mock
    private B2CRequestRepository b2cRequestRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PaymentStatusRepository paymentStatusRepository;

    @Mock
    private KafkaTemplate<String, B2CRequest> kafkaTemplate;

    @Mock
    private OAuthTokenGenerator oAuthTokenGenerator;

    @InjectMocks
    private B2CService b2cService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessB2CRequest_InvalidMobileNumber() {
        UUID id = UUID.randomUUID();
        UserRequest userRequest = new UserRequest();
        userRequest.setId(id);
        userRequest.setMobileNumber("12345678");
        userRequest.setAmount(100.0f);

        Exception exception = assertThrows(BadRequestException.class, () -> {
            b2cService.processB2CRequest(userRequest);
        });

        String expectedMessage = "Invalid Kenyan mobile number. It should start with 2547 and be followed by 8 digits.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFetchPaymentStatus_Found() {
        UUID id = UUID.randomUUID();
        PaymentStatusEntity paymentStatus =  PaymentStatusEntity.builder()
                .status("Completed")
                .transactionId(id)
                .build();

        when(paymentStatusRepository.findById("12345")).thenReturn(Optional.of(paymentStatus));

        PaymentStatusEntity result = b2cService.fetchPaymentStatus("12345");

        assertNotNull(result);
        assertEquals(id, result.getTransactionId());
        assertEquals("Completed", result.getStatus());
    }

    @Test
    public void testFetchPaymentStatus_NotFound() {

        when(paymentStatusRepository.findById("12345")).thenReturn(Optional.empty());

        PaymentStatusEntity result = b2cService.fetchPaymentStatus("12345");

        assertNull(result);
    }

    @Test
    public void testUpdatePaymentStatus_Found() {
        UUID id = UUID.randomUUID();
        PaymentStatusEntity paymentStatus =  PaymentStatusEntity.builder()
                .transactionId(id)
                .status("Pending")
                .build();

        PaymentStatusUpdate update = new PaymentStatusUpdate();
        update.setTransactionID("12345");
        update.setStatus("Completed");
        update.setDetails("Details updated");

        when(paymentStatusRepository.findById("12345")).thenReturn(Optional.of(paymentStatus));

        b2cService.updatePaymentStatus(update);

        verify(paymentStatusRepository, times(1)).save(paymentStatus);
        assertEquals("Completed", paymentStatus.getStatus());
        assertEquals("Details updated", paymentStatus.getDetails());
    }

    @Test
    public void testUpdatePaymentStatus_NotFound() {
        PaymentStatusUpdate update = new PaymentStatusUpdate();
        update.setTransactionID("12345");
        update.setStatus("Completed");
        update.setDetails("Details updated");

        when(paymentStatusRepository.findById("12345")).thenReturn(Optional.empty());

        b2cService.updatePaymentStatus(update);

        verify(paymentStatusRepository, never()).save(any(PaymentStatusEntity.class));
    }
}
