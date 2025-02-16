package com.hancidev.orderservice.service.mapper;

import com.hancidev.orderservice.client.BookServiceClient;
import com.hancidev.orderservice.dto.OrderRequest;
import com.hancidev.orderservice.dto.OrderResponse;
import com.hancidev.orderservice.dto.response.BookApiResponse;
import com.hancidev.orderservice.entity.Order;
import com.hancidev.orderservice.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final BookServiceClient bookServiceClient;

    public Order orderFromOrderRequest(OrderRequest orderRequest) {
        return Order.builder()
                .orderID(UUID.randomUUID().toString())
                .customerID(orderRequest.customerID())
                .orderDate(LocalDateTime.now())
                .orderStatus(Status.APPROVED)
                .productIDs(orderRequest.productIDs())
                .totalPrice(calculateTotalPrice(orderRequest.productIDs()))
                .build();
    }

    public OrderResponse orderResponseFromOrder(Order order) {
        return OrderResponse.builder()
                .orderID(order.getOrderID())
                .customerID(order.getCustomerID())
                .productIDs(order.getProductIDs())
                .orderStatus(order.getOrderStatus().name())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice().doubleValue())
                .build();
    }

    private List<BookApiResponse> getBooks(List<String> productIDs) {
        return productIDs.stream()
                .map(bookServiceClient::findBook)
                .toList();
    }

    private BigDecimal calculateTotalPrice(List<String> productIDs) {
        List<BookApiResponse> bookApiResponses = getBooks(productIDs);
        return setScale(bookApiResponses.stream()
                .map(BookApiResponse::price)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private BigDecimal setScale(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_EVEN);
    }
}
