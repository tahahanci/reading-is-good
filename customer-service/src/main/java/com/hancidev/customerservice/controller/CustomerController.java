package com.hancidev.customerservice.controller;

import com.hancidev.customerservice.dto.CustomerRequest;
import com.hancidev.customerservice.dto.CustomerResponse;
import com.hancidev.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
        return ResponseEntity.ok(customerService.createCustomer(customerRequest));
    }

    @GetMapping("/find")
    public ResponseEntity<CustomerResponse> findCustomer(@RequestParam String customerMail) {
        return ResponseEntity.ok(customerService.findCustomer(customerMail));
    }
}
