package com.hancidev.customerservice.dto;

import lombok.Builder;

@Builder
public record AddressRequest(String street, String city, String state, String zipCode) {
}
