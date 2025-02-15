package com.hancidev.customerservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CustomerRequest(String customerName, String customerSurname,
                              String customerMail, List<AddressRequest> addressRequests) {
}
