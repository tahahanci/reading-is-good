package com.hancidev.orderservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponse(String orderID, String customerID, List<String> productIDs, double totalPrice,
                            String orderStatus, LocalDateTime orderDate) {
}
