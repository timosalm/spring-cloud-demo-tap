package com.example.orderservice.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderApplicationService {

    private static final Logger log = LoggerFactory.getLogger(OrderApplicationService.class);

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final ShippingService shippingService;

    OrderApplicationService(OrderRepository orderRepository, ProductService productService,
                            ShippingService shippingService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.shippingService = shippingService;

        shippingService.setOrderStatusUpdateConsumer(this::updateOrderStatus);
    }

    List<Order> fetchOrders() {
        return orderRepository.findAll();
    }

    Order createOrder(CreateOrderData createOrderData) {
        final Order order = Order.create(createOrderData.getProductId(), createOrderData.getShippingAddress());

        final List<Product> products = productService.fetchProducts();
        order.validate(products);
        orderRepository.save(order);

        log.info("Created order: " + order);

        shippingService.shipOrder(order);

        return order;
    }

    private void updateOrderStatus(OrderStatusUpdate statusUpdate) {
        final Optional<Order> order = orderRepository.findById(statusUpdate.getId());
        order.ifPresent(it -> {
            log.info("Status update with id: " + statusUpdate.getId() + " and status " + statusUpdate.getStatus());
            it.updateStatus(statusUpdate);
            orderRepository.save(it);
        });
        if (order.isEmpty()) {
            log.error("Retrieved status update but with unknown id: " + statusUpdate.getId());
        }
    }
}
