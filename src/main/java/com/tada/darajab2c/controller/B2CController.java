package com.tada.darajab2c.controller;

import com.tada.darajab2c.dto.Payload;
import com.tada.darajab2c.dto.PaymentStatusUpdate;
import com.tada.darajab2c.dto.UserRequest;
import com.tada.darajab2c.entity.PaymentStatusEntity;
import com.tada.darajab2c.service.B2CService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/b2c")
public class B2CController {

    private final B2CService b2cService;

    public B2CController(B2CService b2cService) {
        this.b2cService = b2cService;
    }

    @PostMapping("/request")
    public ResponseEntity<Payload> handleB2CRequest(@RequestBody UserRequest request) {
        Payload payload = b2cService.processB2CRequest(request);
        if ("Failed".equals(payload.getStatus())) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/status/{transactionId}")
    public ResponseEntity<PaymentStatusEntity> getPaymentStatus(@PathVariable String transactionId) {
        PaymentStatusEntity paymentStatusEntity = b2cService.fetchPaymentStatus(transactionId);
        return ResponseEntity.ok(paymentStatusEntity);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updatePaymentStatus(@RequestBody PaymentStatusUpdate statusUpdate) {
        b2cService.updatePaymentStatus(statusUpdate);
        return ResponseEntity.ok("Payment status updated");
    }
}
