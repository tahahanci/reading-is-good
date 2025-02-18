package com.hancidev.orderservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderRequest(String customerID, List<String> productIDs) {
}
