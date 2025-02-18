package com.hancidev.orderservice.service.impl;

import com.hancidev.orderservice.dto.OrderRequest;
import com.hancidev.orderservice.dto.OrderResponse;
import com.hancidev.orderservice.entity.Order;
import com.hancidev.orderservice.exception.OrderNotFoundException;
import com.hancidev.orderservice.repository.OrderRepository;
import com.hancidev.orderservice.service.OrderService;
import com.hancidev.orderservice.service.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderResponse> findOrders(String customerID) {
        List<Order> orders = orderRepository.findByCustomerID(customerID);
        if (orders == null) {
            log.info("Orders did not found!");
            throw new OrderNotFoundException("Order not found!");
        }

        log.info("Orders find, total orders count: {}", orders.size());
        return orders.stream()
                .map(orderMapper::orderResponseFromOrder)
                .toList();
    }

    @Override
    public OrderResponse giveOrder(OrderRequest orderRequest) {
        Order order = orderRepository.save(orderMapper.orderFromOrderRequest(orderRequest));
        log.info("Order created, total basket price: {}", order.getTotalPrice());
        return orderMapper.orderResponseFromOrder(order);
    }


}
