package com.hancidev.orderservice.repository;

import com.hancidev.orderservice.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {

    Optional<Order> findByOrderID(String orderID);

    List<Order> findByCustomerID(String customerID);
}
