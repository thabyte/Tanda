package com.tada.darajab2c.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.protocol.types.Field;

import java.util.UUID;

@Builder
@Getter
@Setter
public class B2CRequest {
    @JsonProperty("OriginatorConversationID")
    private UUID originatorConversationID;

    @JsonProperty("InitiatorName")
    private String initiatorName;

    @JsonProperty("SecurityCredential")
    private String securityCredential;

    @JsonProperty("CommandID")
    private String commandID;

    @JsonProperty("Amount")
    private float amount;

    @JsonProperty("PartyA")
    private int partyA;

    @JsonProperty("PartyB")
    private String partyB;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("QueueTimeOutURL")
    private String queueTimeOutURL;

    @JsonProperty("ResultURL")
    private String resultURL;

    @JsonProperty("Occasion")
    private String occasion;

    @JsonProperty("Status")
    private String status;
}
