package com.hancidev.orderservice.controller;

import com.hancidev.orderservice.dto.OrderRequest;
import com.hancidev.orderservice.dto.OrderResponse;
import com.hancidev.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> giveOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.giveOrder(request));
    }

    @GetMapping("/show/{customerID}")
    public ResponseEntity<List<OrderResponse>> showOrders(@PathVariable String customerID) {
        return ResponseEntity.ok(orderService.findOrders(customerID));
    }
}
