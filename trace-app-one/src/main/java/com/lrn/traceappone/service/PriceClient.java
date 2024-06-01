package com.lrn.traceappone.service;

import com.lrn.traceappone.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class PriceClient {

    private final RestTemplate restTemplate;

    private final WebClient webClient;

//    private final String baseUrl = "http://price-service:8085";
    private final String baseUrl = "http://localhost:8085";

    public PriceClient(RestTemplate restTemplate, WebClient webClient) {
        this.restTemplate = restTemplate;
        this.webClient = webClient;
    }

    public Price getPrice(@PathVariable("id") long productId){
        log.info("Fetching Price Details With Product Id {}", productId);
        String url = String.format("%s/price/%d", baseUrl, productId);
        ResponseEntity<Price> price = this.webClient.get().uri(url).retrieve().toEntity(Price.class).block();
        //ResponseEntity<Price> price = this.restTemplate.getForEntity(url, Price.class);
        assert price != null;
        return price.getBody();
    }
}
