package com.tada.darajab2c.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;


@Document(collection = "payment_statuses")
@Builder
@Getter
@Setter
public class PaymentStatusEntity {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("transactionId")
    private UUID transactionId;

    @JsonProperty("originatorConversationID")
    private String originatorConversationID;

    @JsonProperty("resultCode")
    private String resultCode;

    @JsonProperty("resultDesc")
    private String resultDesc;

    @JsonProperty("details")
    private String details;

    @JsonProperty("createdDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonProperty("updatedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedDate;

    @JsonProperty("status")
    private String status;
}