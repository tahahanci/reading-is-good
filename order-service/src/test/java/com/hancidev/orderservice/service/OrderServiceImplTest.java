package com.hancidev.orderservice.service;

import com.hancidev.orderservice.dto.OrderRequest;
import com.hancidev.orderservice.dto.OrderResponse;
import com.hancidev.orderservice.entity.Order;
import com.hancidev.orderservice.entity.Status;
import com.hancidev.orderservice.exception.OrderNotFoundException;
import com.hancidev.orderservice.repository.OrderRepository;
import com.hancidev.orderservice.service.impl.OrderServiceImpl;
import com.hancidev.orderservice.service.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequest orderRequest;
    private Order order;
    private OrderResponse orderResponse;

    private List<OrderResponse> orderResponses;
    private List<Order> orders;
    private List<Order> emptyOrders;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .orderID("A1234567")
                .orderDate(LocalDateTime.now())
                .id("ASD61263")
                .orderStatus(Status.APPROVED)
                .orderDate(LocalDateTime.now())
                .customerID("A12345")
                .productIDs(List.of("A2678", "A2778"))
                .totalPrice(new BigDecimal("100.00"))
                .build();

        Order order2 = Order.builder()
                .orderID("A1234567")
                .orderDate(LocalDateTime.now())
                .id("ASD61263")
                .orderStatus(Status.APPROVED)
                .orderDate(LocalDateTime.now())
                .customerID("A12345")
                .productIDs(List.of("A2678", "A2778"))
                .totalPrice(new BigDecimal("100.00"))
                .build();

        orderRequest = OrderRequest.builder()
                .customerID("A12345")
                .productIDs(List.of("A2678", "A2778"))
                .build();

        orderResponse = OrderResponse.builder()
                .orderID("A1234567")
                .orderDate(LocalDateTime.now())
                .customerID("A12345")
                .productIDs(List.of("A2678", "A2778"))
                .totalPrice(100.00)
                .orderStatus("APPROVED")
                .build();

        OrderResponse orderResponse2 = OrderResponse.builder()
                .orderID("A1234567")
                .orderDate(LocalDateTime.now())
                .customerID("A12345")
                .productIDs(List.of("A2678", "A2778"))
                .totalPrice(100.00)
                .orderStatus("APPROVED")
                .build();

        orderResponses = List.of(orderResponse, orderResponse2);
        orders = List.of(order, order2);
        emptyOrders = List.of();
    }

    @Test
    void giveOrder_WhenOrderRequestsValid_ShouldReturnOrderResponse() {
        when(orderMapper.orderFromOrderRequest(orderRequest)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.orderResponseFromOrder(order)).thenReturn(orderResponse);

        OrderResponse actualResponse = orderService.giveOrder(orderRequest);

        assertThat(actualResponse)
                .isNotNull()
                .isEqualTo(orderResponse);

        verify(orderMapper, times(1)).orderFromOrderRequest(orderRequest);
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).orderResponseFromOrder(order);
    }

    @Test
    void findOrders_WhenOrdersExist_ShouldReturnOrderResponseList() {
        String customerID = "A12345";

        when(orderRepository.findByCustomerID(customerID)).thenReturn(orders);
        when(orderMapper.orderResponseFromOrder(any(Order.class)))
                .thenReturn(orderResponses.get(0), orderResponses.get(1));

        List<OrderResponse> actualResponses = orderService.findOrders(customerID);

        assertThat(actualResponses)
                .isNotNull()
                .hasSize(2)
                .isEqualTo(orderResponses);

        verify(orderRepository).findByCustomerID(customerID);
        verify(orderMapper, times(2)).orderResponseFromOrder(any(Order.class));
    }

    @Test
    void findOrders_WhenNoOrders_ShouldThrowOrderNotFoundException() {
        String customerID = "nonexistent";
        when(orderRepository.findByCustomerID(customerID)).thenReturn(null);

        assertThatThrownBy(() -> orderService.findOrders(customerID))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("Order not found!");

        verify(orderRepository).findByCustomerID(customerID);
        verify(orderMapper, never()).orderResponseFromOrder(any());
    }

    @Test
    void findOrders_WhenEmptyOrderList_ShouldReturnEmptyList() {
        String customerId = "customer123";

        when(orderRepository.findByCustomerID(customerId)).thenReturn(emptyOrders);

        List<OrderResponse> result = orderService.findOrders(customerId);

        assertThat(result)
                .isNotNull()
                .isEmpty();

        verify(orderRepository).findByCustomerID(customerId);
        verify(orderMapper, never()).orderResponseFromOrder(any());
    }


}
