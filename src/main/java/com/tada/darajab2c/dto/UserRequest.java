package com.tada.darajab2c.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserRequest {
    @JsonProperty("ID")
    private UUID id;

    @JsonProperty("Amount")
    private float amount;

    @JsonProperty("MobileNumber")
    private String mobileNumber;
}

