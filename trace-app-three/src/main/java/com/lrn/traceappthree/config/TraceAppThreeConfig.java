package com.lrn.traceappthree.config;

import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.web.reactive.client.ObservationWebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TraceAppThreeConfig {

    @Bean
    public OtlpHttpSpanExporter otlpHttpSpanExporter(@Value("${tracing.url}") String url) {
        return OtlpHttpSpanExporter.builder()
                .setEndpoint(url)
                .build();
    }

    @Bean
    public ConsumerFactory<Integer, String> integerStringConsumerFactory() {
        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(consumerConfig);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, String> integerStringConcurrentKafkaListenerContainerFactory(ConsumerFactory<Integer, String> integerStringConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Integer, String> integerStringConcurrentKafkaListenerContainerFactory
                = new ConcurrentKafkaListenerContainerFactory<>();
        integerStringConcurrentKafkaListenerContainerFactory.setConsumerFactory(integerStringConsumerFactory);
        //integerStringConcurrentKafkaListenerContainerFactory.setBatchListener(true);
        integerStringConcurrentKafkaListenerContainerFactory.getContainerProperties().setObservationEnabled(true);
        return integerStringConcurrentKafkaListenerContainerFactory;
    }

    @Bean
    public WebClient webClientFromBuilder(ObservationWebClientCustomizer observationWebClientCustomizer, WebClient.Builder webClientFromBuilder) {
        observationWebClientCustomizer.customize(webClientFromBuilder);
        return webClientFromBuilder.build();
    }
}
