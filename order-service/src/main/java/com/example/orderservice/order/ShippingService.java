package com.example.orderservice.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class ShippingService {

    private static final String ORDER_QUEUE_INPUT_NAME = "order-in-queue";
    private static final Logger log = LoggerFactory.getLogger(ShippingService.class);

    @Value("${order.exchange-name}")
    private String orderExchangeName;

    @Value("${order.shipping-exchange-name}")
    private String orderShippingExchangeName;

    private final RabbitTemplate rabbitTemplate;
    private Consumer<OrderStatusUpdate> orderStatusUpdateConsumer;

    ShippingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    Queue orderInQueue() {
        return new Queue(ORDER_QUEUE_INPUT_NAME, false);
    }

    @Bean
    TopicExchange exchange() {
        if (orderExchangeName == null || orderExchangeName.isEmpty()) {
            throw new RuntimeException("order.exchange-name not set");
        }
        return new TopicExchange(orderExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("#");
    }

    void shipOrder(Order order) {
        if (orderShippingExchangeName == null || orderShippingExchangeName.isEmpty()) {
            throw new RuntimeException("order.shipping-exchange-name not set");
        }
        rabbitTemplate.convertAndSend(orderShippingExchangeName, "#", order);
        log.info("Order with id " + order.getId() + " shipped");
    }

    @RabbitListener(queues = ORDER_QUEUE_INPUT_NAME)
    private void updateStatus(OrderStatusUpdate statusUpdate) {
        System.out.println("updateStatus called for order id: " + statusUpdate.getId() + " with status "
                + statusUpdate.getStatus());
        if (orderStatusUpdateConsumer != null) {
            orderStatusUpdateConsumer.accept(statusUpdate);
        }
    }

    void setOrderStatusUpdateConsumer(Consumer<OrderStatusUpdate> orderStatusUpdateConsumer) {
        this.orderStatusUpdateConsumer = orderStatusUpdateConsumer;
    }
}
