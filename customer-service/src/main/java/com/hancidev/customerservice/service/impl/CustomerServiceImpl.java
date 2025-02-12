package com.hancidev.customerservice.service.impl;

import com.hancidev.customerservice.dto.CustomerRequest;
import com.hancidev.customerservice.dto.CustomerResponse;
import com.hancidev.customerservice.entity.Customer;
import com.hancidev.customerservice.exception.CustomerAlreadyExistException;
import com.hancidev.customerservice.exception.CustomerNotFoundException;
import com.hancidev.customerservice.repository.CustomerRepository;
import com.hancidev.customerservice.service.CustomerService;
import com.hancidev.customerservice.service.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        if (customerRepository.findByCustomerMail(customerRequest.customerMail()).isPresent()) {
            log.error("Customer already exists with given mail: {}", customerRequest.customerMail());
            throw new CustomerAlreadyExistException("Customer already exists with given mail");
        }

        Customer customer = customerRepository.save(customerMapper.customerFromCustomerRequest(customerRequest));
        log.info("Customer saved with ID: {}", customer.getId());
        return customerMapper.customerResponseFromCustomer(customer);
    }

    @Override
    public CustomerResponse findCustomer(String customerMail) {
        Customer customer = customerRepository.findByCustomerMail(customerMail)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with given mail"));
        return customerMapper.customerResponseFromCustomer(customer);
    }
}
