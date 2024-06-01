package com.lrn.traceappone.config;

import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.web.reactive.client.ObservationWebClientCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TraceAppOneConfig {

    @Bean
    public OtlpHttpSpanExporter otlpHttpSpanExporter(@Value("${tracing.url}") String url) {
        return OtlpHttpSpanExporter.builder()
                .setEndpoint(url)
                .build();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

//    @Bean
//    public WebClient webClient(ObservationWebClientCustomizer observationWebClientCustomize) {
//        return WebClient.builder().build();
//    }

    @Bean
    public WebClient webClientFromBuilder(ObservationWebClientCustomizer observationWebClientCustomizer, WebClient.Builder webClientFromBuilder) {
        observationWebClientCustomizer.customize(webClientFromBuilder);
        return webClientFromBuilder.build();
    }

    @Bean
    public ProducerFactory<Integer, String> integerStringProducerFactory() {
        Map<String, Object> producerConfig = new HashMap<>();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(producerConfig);
    }

    @Bean
    public KafkaTemplate<Integer, String> integerStringKafkaTemplate(ProducerFactory<Integer, String> integerStringProducerFactory) {
        KafkaTemplate<Integer, String> integerStringKafkaTemplate = new KafkaTemplate<>(integerStringProducerFactory);
        integerStringKafkaTemplate.setObservationEnabled(true);
        return integerStringKafkaTemplate;
    }
}
