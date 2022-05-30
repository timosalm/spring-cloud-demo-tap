package com.example.orderservice.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.ValidationException;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "`Order`") // Order is a reserved word
public class Order implements Serializable { // Serializable required for Redis cache

    private static final long serialVersionUID = -1122620860862785845L;

    @Id
    @GeneratedValue
    private Long id;
    private Long productId;
    private OrderStatus orderStatus = OrderStatus.CREATED;

    private String shippingAddress;

    private Order() {
    }

    private Order(Long productId, String shippingAddress) {
        this.productId = productId;
        this.shippingAddress = shippingAddress;
    }

    static Order create(Long productId, String shippingAddress) {
        return new Order(productId, shippingAddress);
    }

    void validate(List<Product> products) {
        if (products.stream().noneMatch(product -> product.getId().equals(productId))) {
            throw new ValidationException("Unknown product with id: " + productId);
        }
    }

    void updateStatus(OrderStatusUpdate statusUpdate) {
        if (id.equals(statusUpdate.getId())) {
            this.orderStatus = statusUpdate.getStatus();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", productId=" + productId + ", orderStatus=" + orderStatus + ", shippingAddress='" +
                shippingAddress + '\'' + '}';
    }
}
