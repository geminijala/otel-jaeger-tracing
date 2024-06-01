package com.lrn.traceapptwo.service;

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

    private final WebClient webClient;

    private final KafkaTemplate<Integer, String> integerStringKafkaTemplate;

    private Integer messageKeyCount = 0;

    private final String baseUrl = "http://localhost:8084";

    public KafkaService(WebClient webClient, KafkaTemplate<Integer, String> integerStringKafkaTemplate) {
        this.webClient = webClient;
        this.integerStringKafkaTemplate = integerStringKafkaTemplate;
    }

    @KafkaListener(topics = {"trace-apps-one-two-topic"}, groupId = "trace-app-two", containerFactory = "integerStringConcurrentKafkaListenerContainerFactory")
    public void consumeMessages(ConsumerRecord<Integer, String> integerStringConsumerRecord) {
        try {
            /*integerStringConsumerRecords.forEach(integerStringConsumerRecord -> {
                log.info("Key is: " + integerStringConsumerRecord.key());
                log.info("Value is: " + integerStringConsumerRecord.value());
            });*/
            log.info("Key is: " + integerStringConsumerRecord.key());
            log.info("Value is: " + integerStringConsumerRecord.value());

            log.info("Fetching Price Details With Product Id {}", 1);
            String url = String.format("%s/product/%d", baseUrl, 1);
            ResponseEntity<Object> product = this.webClient.get().uri(url).retrieve().toEntity(Object.class).block();
            assert product != null;
            System.out.println(product.getBody());

            String message = "Message @ " + LocalDateTime.now();
            this.integerStringKafkaTemplate.send("trace-apps-two-three-topic", ++this.messageKeyCount, message);
        } catch (Exception e) {
            log.error("Exception occurred when consuming messages", e);
        }
    }
}

