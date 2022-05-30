package com.example.shippingservice.shipping;

class OrderStatusUpdate {

    private Long id;
    private OrderStatus status;

    private OrderStatusUpdate() {
    }

    private OrderStatusUpdate(Long id, OrderStatus status) {
        this.id = id;
        this.status = status;
    }

    static OrderStatusUpdate forDelivered(Long id) {
        return new OrderStatusUpdate(id, OrderStatus.DELIVERED);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
