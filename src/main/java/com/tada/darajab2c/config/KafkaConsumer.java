package com.tada.darajab2c.config;

import com.tada.darajab2c.dto.B2CRequest;
import com.tada.darajab2c.service.B2CService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {


    private final B2CService service;

    public KafkaConsumer(B2CService service) {
        this.service = service;
    }

    @KafkaListener(topics = "${spring.kafka.topic.b2c-responses}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) {

        B2CRequest request = B2CRequest.builder().build();
        request.setStatus("Processed");
        service.updateRequest(request);
        System.out.println("Received Message: " + message);
    }
}
