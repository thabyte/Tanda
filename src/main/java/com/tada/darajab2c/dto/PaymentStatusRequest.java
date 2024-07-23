package com.tada.darajab2c.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public class PaymentStatusRequest {

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
