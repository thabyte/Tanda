package com.tada.darajab2c.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Payload {
    @JsonProperty("ID")
    private UUID id;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Ref")
    private String ref;
}
