package com.hancidev.customerservice.dto;

import lombok.Builder;

@Builder
public record AddressResponse(String street, String city, String state, String zipCode) {
}

