package com.tada.darajab2c.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "b2c_requests")
public class B2CRequestEntity {

    @Id
    private String id;
    @JsonProperty("OriginatorConversationID")
    private String originatorConversationID;

    @JsonProperty("InitiatorName")
    private String initiatorName;

    @JsonProperty("SecurityCredential")
    private String securityCredential;

    @JsonProperty("CommandID")
    private String commandID;

    @JsonProperty("Amount")
    private int amount;

    @JsonProperty("PartyA")
    private int partyA;

    @JsonProperty("PartyB")
    private long partyB;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("QueueTimeOutURL")
    private String queueTimeOutURL;

    @JsonProperty("ResultURL")
    private String resultURL;

    @JsonProperty("Occasion")
    private String occasion;
}
