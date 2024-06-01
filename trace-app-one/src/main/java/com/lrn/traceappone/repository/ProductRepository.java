package com.lrn.traceappone.repository;

import com.lrn.traceappone.exceptions.ProductNotFoundException;
import com.lrn.traceappone.model.Product;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class ProductRepository {

    private final Map<Long, Product> productMap = new HashMap<>();

    @PostConstruct
    public void postConstruct() {
//        Price price = new Price();
//        price.setProductId(1);
//        price.setPriceAmount(1.0);
//        price.setDiscount(0.0);

        Product product = new Product();
        product.setPrice(null);
        product.setId(1);
        product.setName("One");

        this.productMap.put(1L, product);
    }

    public Product getProduct(Long productId) {
        log.info("Getting Product from Product Repo With Product Id {}", productId);
        if(!productMap.containsKey(productId)){
            log.error("Product Not Found for Product Id {}", productId);
            throw new ProductNotFoundException("Product Not Found");
        }
        return productMap.get(productId);
    }
}
