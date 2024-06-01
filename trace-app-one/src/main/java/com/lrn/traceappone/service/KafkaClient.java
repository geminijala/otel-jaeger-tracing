package com.lrn.traceappone.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class KafkaClient {

    private final KafkaTemplate<Integer, String> integerStringKafkaTemplate;
    private Integer messageKeyCount = 0;

    public KafkaClient(KafkaTemplate<Integer, String> integerStringKafkaTemplate) {
        this.integerStringKafkaTemplate = integerStringKafkaTemplate;
    }

    public String sendMessage() {
        String message = "Message @ " + LocalDateTime.now();
        this.integerStringKafkaTemplate.send("trace-apps-one-two-topic", ++this.messageKeyCount, message);
        return message;
    }
}
