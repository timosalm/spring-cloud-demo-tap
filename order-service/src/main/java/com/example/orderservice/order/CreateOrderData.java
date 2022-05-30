package com.example.orderservice.order;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class CreateOrderData {

    @Min(0)
    @Max(Integer.MAX_VALUE)
    private Long productId;
    @NotEmpty
    private String shippingAddress;

    public CreateOrderData() {
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
}
