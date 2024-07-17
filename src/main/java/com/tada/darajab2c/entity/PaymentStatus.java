package com.tada.darajab2c.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "payment_statuses")
@Getter
@Setter
public class PaymentStatus {
    private UUID transactionId;
    private String originatorConversationID;
    private String resultCode;
    private String resultDesc;
    private String details;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private String status;

}
