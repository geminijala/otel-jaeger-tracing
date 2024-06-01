package com.lrn.traceappthree.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Service
@Slf4j
public class KafkaService {

    @KafkaListener(topics = {"trace-apps-two-three-topic"}, groupId = "trace-app-three", containerFactory = "integerStringConcurrentKafkaListenerContainerFactory")
    public void consumeMessages(ConsumerRecord<Integer, String> integerStringConsumerRecord) {
        try {
            log.info("Key is: " + integerStringConsumerRecord.key());
            log.info("Value is: " + integerStringConsumerRecord.value());
        } catch (Exception e) {
            log.error("Exception occurred when consuming messages", e);
        }
    }
}

