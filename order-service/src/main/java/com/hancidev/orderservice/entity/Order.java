package com.hancidev.orderservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    private String id;
    private String orderID;
    private String customerID;
    private List<String> productIDs;
    private BigDecimal totalPrice;
    private Status orderStatus;
    private LocalDateTime orderDate;
}
