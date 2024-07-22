package com.tada.darajab2c.service;

import com.tada.darajab2c.dto.*;
import com.tada.darajab2c.entity.PaymentStatus;
import com.tada.darajab2c.exception.BadRequestException;
import com.tada.darajab2c.repository.B2CRequestRepository;
import com.tada.darajab2c.repository.PaymentStatusRepository;
import com.tada.darajab2c.util.OAuthTokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class B2CService {

    private final B2CRequestRepository b2cRequestRepository;
    private final RestTemplate restTemplate;
    private final PaymentStatusRepository paymentStatusRepository;
    private final KafkaTemplate<String, B2CRequest> kafkaTemplate;
    private final OAuthTokenGenerator oAuthTokenGenerator;

    @Value("${spring.kafka.topic.b2c-requests}")
    private String b2cRequestsTopic;

//    @Value("${spring.kafka.topic.payment-status}")
//    private String paymentStatusTopic;

    public B2CService(B2CRequestRepository b2cRequestRepository, RestTemplate restTemplate, PaymentStatusRepository paymentStatusRepository, KafkaTemplate<String, B2CRequest> kafkaTemplate, OAuthTokenGenerator oAuthTokenGenerator) {
        this.b2cRequestRepository = b2cRequestRepository;
        this.restTemplate = restTemplate;
        this.paymentStatusRepository = paymentStatusRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.oAuthTokenGenerator = oAuthTokenGenerator;
    }

    public Payload processB2CRequest(UserRequest request) {
        try {
            validateUserRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage(), e);
        }


        B2CRequest b2cRequest = B2CRequest.builder()
                .originatorConversationID(request.getId())
                .initiatorName("testapi")
                .securityCredential(oAuthTokenGenerator.generateSecurityCredential("Safaricom999!*!"))
                .commandID("BusinessPayment")
                .amount(request.getAmount())
                .partyA(600426)
                .partyB(request.getMobileNumber())
                .remarks("Test remarks")
                .queueTimeOutURL("https://mydomain.com/b2c/queue")
                .resultURL("https://mydomain.com/b2c/result")
                .occasion("Null")
                .build();



        String url = "https://sandbox.safaricom.co.ke/mpesa/b2c/v1/paymentrequest";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            String accessToken = oAuthTokenGenerator.getOAuthToken();
            headers.setBearerAuth(accessToken);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to obtain OAuth token", e);
        }

        HttpEntity<B2CRequest> requestEntity = new HttpEntity<>(b2cRequest, headers);

        try {
            ResponseEntity<B2CResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, B2CResponse.class);
            B2CResponse response = responseEntity.getBody();

            b2cRequestRepository.save(b2cRequest);

            logPaymentRequest(b2cRequest, response);

            Payload payload = new Payload();
            payload.setId(UUID.randomUUID());
            payload.setStatus("Success");
            payload.setRef(response != null ? response.getConversationID() : "Unknown");

            return payload;
        } catch (Exception e) {
            logFailedRequest(b2cRequest, e);


            Payload payload = new Payload();
            payload.setId(UUID.randomUUID());
            payload.setStatus("Failed");
            payload.setRef("Unknown");
            return payload;
        }
    }

    private void validateUserRequest(UserRequest request) {
        String mobileNumber = request.getMobileNumber();
        if (mobileNumber == null || mobileNumber.isEmpty()) {
            throw new IllegalArgumentException("Mobile number must be provided");
        }
        if (!mobileNumber.matches("^2547\\d{8}$")) {
            throw new IllegalArgumentException("Invalid Kenyan mobile number. It should start with 2547 and be followed by 8 digits.");
        }

        Float amount = request.getAmount();
        if (amount == null) {
            throw new IllegalArgumentException("Amount must be provided");
        }
        if (amount < 10 || amount > 150000) {
            throw new IllegalArgumentException("Amount should be between KSh 10 and KSh 150,000.");
        }
    }

    private void logPaymentRequest(B2CRequest b2cRequest, B2CResponse response) {
        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setTransactionId(b2cRequest.getOriginatorConversationID());
        paymentStatus.setStatus("Pending");
        paymentStatus.setDetails(b2cRequest.toString() + " Response: " + (response != null ? response.toString() : "No response"));
        paymentStatus.setCreatedDate(LocalDateTime.now());
        paymentStatusRepository.save(paymentStatus);

        kafkaTemplate.send(b2cRequestsTopic, b2cRequest);
    }

    private void logFailedRequest(B2CRequest b2cRequest, Exception e) {
        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setTransactionId(b2cRequest.getOriginatorConversationID());
        paymentStatus.setStatus("Failed");
        paymentStatus.setDetails("Request: " + b2cRequest.toString() + " Error: " + e.getMessage());
        paymentStatus.setCreatedDate(LocalDateTime.now());
        paymentStatusRepository.save(paymentStatus);

        kafkaTemplate.send(b2cRequestsTopic, b2cRequest);
    }

    public PaymentStatus fetchPaymentStatus(String transactionId) {
        return paymentStatusRepository.findById(transactionId).orElse(null);
    }

    public void updatePaymentStatus(PaymentStatusUpdate statusUpdate) {
        PaymentStatus paymentStatus = paymentStatusRepository.findById(statusUpdate.getTransactionID()).orElse(null);
        if (paymentStatus != null) {
            paymentStatus.setStatus(statusUpdate.getStatus());
            paymentStatus.setDetails(statusUpdate.getDetails());
            paymentStatus.setUpdatedDate(LocalDateTime.now());
            paymentStatusRepository.save(paymentStatus);
        }
    }

    public void handleCallback(Payload callbackPayload) {

    }

    public void queryPaymentStatus() {

    }

    public void updateRequest(B2CRequest request) {

    }
}
