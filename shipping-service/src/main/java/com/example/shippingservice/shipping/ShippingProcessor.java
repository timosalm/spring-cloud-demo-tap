package com.example.shippingservice.shipping;

import com.example.shippingservice.ShippingServiceApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RefreshScope
@Component
public class ShippingProcessor {

    private static final Logger log = LoggerFactory.getLogger(ShippingServiceApplication.class);

    @Value("${shipping.duration}")
    private int shippingDuration;

    @Bean
    public Function<Order, OrderStatusUpdate> shipOrder() {
        return order -> {
            log.info("shipOrder called for order id: " + order.getId() + " and shipping duration " + shippingDuration);
            try {
                Thread.sleep(shippingDuration);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return OrderStatusUpdate.forDelivered(order.getId());
        };
    }
}
