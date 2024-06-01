package com.lrn.traceapptwo.repository;

import com.lrn.traceapptwo.exceptions.PriceNotFoundException;
import com.lrn.traceapptwo.model.Price;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class PriceRepository {

    private final Map<Long, Price> priceMap = new HashMap<>();

    @PostConstruct
    public void postConstruct() {
        Price price = new Price();
        price.setProductId(1);
        price.setPriceAmount(1.0);
        price.setDiscount(0.0);

        this.priceMap.put(1L, price);
    }

    public Price getPrice(Long productId){
        log.info("Getting Price from Price Repo With Product Id {}", productId);
        if(!priceMap.containsKey(productId)){
            log.error("Price Not Found for Product Id {}", productId);
            throw new PriceNotFoundException("Price Not Found");
        }
        return priceMap.get(productId);
    }
}
