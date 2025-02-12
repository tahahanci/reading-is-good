package com.hancidev.customerservice.service.mapper;

import com.hancidev.customerservice.dto.AddressResponse;
import com.hancidev.customerservice.dto.CustomerRequest;
import com.hancidev.customerservice.dto.CustomerResponse;
import com.hancidev.customerservice.entity.Address;
import com.hancidev.customerservice.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerMapper {

    public Customer customerFromCustomerRequest(CustomerRequest from) {
        Customer customer = Customer.builder()
                .customerMail(from.customerMail())
                .customerName(from.customerName())
                .customerSurname(from.customerSurname())
                .addresses(new ArrayList<>())
                .build();

        if (from.addressRequests() != null) {
            from.addressRequests().forEach(addressRequest -> {
                Address address = Address.builder()
                        .street(addressRequest.street())
                        .city(addressRequest.city())
                        .zipCode(addressRequest.zipCode())
                        .state(addressRequest.state())
                        .customer(customer)
                        .build();
                customer.getAddresses().add(address);
            });
        }
        return customer;
    }

    public CustomerResponse customerResponseFromCustomer(Customer from) {
        return CustomerResponse.builder()
                .customerID(from.getId())
                .customerName(from.getCustomerName())
                .customerSurname(from.getCustomerSurname())
                .customerMail(from.getCustomerMail())
                .addressResponses(from.getAddresses() != null ?
                        from.getAddresses().stream()
                                .map(this::addressResponseFromAddress)
                                .toList()
                        : List.of())
                .build();
    }

    private AddressResponse addressResponseFromAddress(Address from) {
        return AddressResponse.builder()
                .city(from.getCity())
                .state(from.getState())
                .street(from.getStreet())
                .zipCode(from.getZipCode())
                .build();
    }
}
