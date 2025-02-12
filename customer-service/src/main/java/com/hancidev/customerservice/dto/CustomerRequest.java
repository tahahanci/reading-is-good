package com.hancidev.customerservice.dto;

import java.util.List;

public record CustomerRequest(String customerName, String customerSurname,
                              String customerMail, List<AddressRequest> addressRequests) {
}
