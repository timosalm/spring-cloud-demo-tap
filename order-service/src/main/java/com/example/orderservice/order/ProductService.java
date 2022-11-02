package com.example.orderservice.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RefreshScope
@Service
class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final RestTemplate restTemplate;
    private final CircuitBreakerFactory circuitBreakerFactory;


    ProductService(RestTemplate restTemplate, CircuitBreakerFactory circuitBreakerFactory) {
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Cacheable("Products")
    public List<Product> fetchProducts() {

        var headers = getAuthenticationHttpHeaders();
        return circuitBreakerFactory.create("products").run(() ->
                        Arrays.asList(Objects.requireNonNull(
                                restTemplate.exchange("http://product-service/api/v1/products", HttpMethod.GET, new HttpEntity<>(null, headers), Product[].class).getBody()
                        )),
                throwable -> {
                    log.error("Call to product service failed, using empty product list as fallback", throwable);
                    return Collections.emptyList();
                });
    }

    private HttpHeaders getAuthenticationHttpHeaders() {
        var headers = new HttpHeaders();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var token = (AbstractOAuth2Token) authentication.getCredentials();
        headers.setBearerAuth(token.getTokenValue());
        return headers;
    }
}