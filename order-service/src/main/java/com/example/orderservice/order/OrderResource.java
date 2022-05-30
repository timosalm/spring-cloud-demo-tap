package com.example.orderservice.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(OrderResource.BASE_URI)
class OrderResource {

    static final String BASE_URI = "/api/v1/orders";

    private final OrderApplicationService orderApplicationService;

    OrderResource(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> fetchOrders() {
        return ResponseEntity.ok(orderApplicationService.fetchOrders());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@NotNull @Valid @RequestBody CreateOrderData createOrderData) {
        final Order order = orderApplicationService.createOrder(createOrderData);
        final URI orderUri = URI.create(String.format("%s/%s", BASE_URI, order.getId()));
        return ResponseEntity.created(orderUri).body(order);
    }
}
