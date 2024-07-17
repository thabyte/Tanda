package com.tada.darajab2c.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.b2c-requests}")
    private String b2cRequestsTopic;

    @Value("${spring.kafka.topic.b2c-responses}")
    private String b2cResponsesTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(Map.of("bootstrap.servers", "localhost:9092"));
    }

    @Bean
    public NewTopic b2cRequestsTopic() {
        return new NewTopic(b2cRequestsTopic, 3, (short) 1);
    }

    @Bean
    public NewTopic b2cResponsesTopic() {
        return new NewTopic(b2cResponsesTopic, 3, (short) 1);
    }
}

