package com.tada.darajab2c.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentStatusUpdate {

    private String transactionID;
    private String originatorConversationID;
    private String resultCode;
    private String resultDesc;
    private String status;
    private String details;

}
