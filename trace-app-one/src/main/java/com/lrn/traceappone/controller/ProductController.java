package com.lrn.traceappone.controller;


import com.lrn.traceappone.model.Product;
import com.lrn.traceappone.repository.ProductRepository;
import com.lrn.traceappone.service.KafkaClient;
import com.lrn.traceappone.service.PriceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ProductController {

    private final PriceClient priceClient;

    private final ProductRepository productRepository;

    private final KafkaClient kafkaClient;

    public ProductController(PriceClient priceClient, ProductRepository productRepository, KafkaClient kafkaClient) {
        this.priceClient = priceClient;
        this.productRepository = productRepository;
        this.kafkaClient = kafkaClient;
    }

    @GetMapping(path = "/product/{id}")
    public Product getProductDetails(@PathVariable("id") long productId){
        log.info("Getting Product and Price Details with Product Id {}", productId);
        Product product = productRepository.getProduct(productId);
        product.setPrice(priceClient.getPrice(productId));
        return product;
    }

    @GetMapping("/sendMessage")
    public ResponseEntity<String> sendMessage() {
        String message = this.kafkaClient.sendMessage();
        return ResponseEntity.ok().body(message);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<String> exceptionHandler(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}