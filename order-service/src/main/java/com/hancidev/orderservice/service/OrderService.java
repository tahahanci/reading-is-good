package com.hancidev.orderservice.service;

import com.hancidev.orderservice.dto.OrderRequest;
import com.hancidev.orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    List<OrderResponse> findOrders(String customerID);

    OrderResponse giveOrder(OrderRequest orderRequest);
}
