package com.hancidev.customerservice.service;

import com.hancidev.customerservice.dto.CustomerRequest;
import com.hancidev.customerservice.dto.CustomerResponse;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest customerRequest);

    CustomerResponse findCustomer(String customerMail);
}
