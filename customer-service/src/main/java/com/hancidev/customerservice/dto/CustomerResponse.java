package com.hancidev.customerservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CustomerResponse(Long customerID, String customerName, String customerSurname, String customerMail,
                               List<AddressResponse> addressResponses) {
}
