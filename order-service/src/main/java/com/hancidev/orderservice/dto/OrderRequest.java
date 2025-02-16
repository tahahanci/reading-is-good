package com.hancidev.orderservice.dto;

import java.util.List;

public record OrderRequest(String customerID, List<String> productIDs) {
}
