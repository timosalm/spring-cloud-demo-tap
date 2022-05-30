package com.example.productservice.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RefreshScope
@Service
public class ProductApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ProductApplicationService.class);

    @Value("${product-service.product-names}")
    private List<String> productNames;

    List<Product> fetchProducts() {
        log.info("Fetch products called");
        return productNames.stream()
                .map(name -> Product.create((long) (productNames.indexOf(name) + 1), name))
                .collect(Collectors.toList());
    }
}
