package com.hancidev.customerservice.service;

import com.hancidev.customerservice.dto.AddressRequest;
import com.hancidev.customerservice.dto.AddressResponse;
import com.hancidev.customerservice.dto.CustomerRequest;
import com.hancidev.customerservice.dto.CustomerResponse;
import com.hancidev.customerservice.entity.Address;
import com.hancidev.customerservice.entity.Customer;
import com.hancidev.customerservice.exception.CustomerAlreadyExistException;
import com.hancidev.customerservice.exception.CustomerNotFoundException;
import com.hancidev.customerservice.repository.CustomerRepository;
import com.hancidev.customerservice.service.impl.CustomerServiceImpl;
import com.hancidev.customerservice.service.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerResponse customerResponse;
    private CustomerRequest customerRequest;

    @BeforeEach
    void setUp() {
        // Address entity setup
        Address address1 = Address.builder()
                .id(1L)
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .build();

        Address address2 = Address.builder()
                .id(2L)
                .street("456 Park Ave")
                .city("Boston")
                .state("MA")
                .zipCode("02101")
                .build();

        // Customer entity setup
        customer = Customer.builder()
                .id(1L)
                .customerName("John")
                .customerSurname("Doe")
                .customerMail("john.doe@example.com")
                .addresses(Arrays.asList(address1, address2))
                .build();

        // Address response setup
        AddressResponse addressResponse1 = AddressResponse.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .build();

        AddressResponse addressResponse2 = AddressResponse.builder()
                .street("456 Park Ave")
                .city("Boston")
                .state("MA")
                .zipCode("02101")
                .build();

        // Customer response setup
        customerResponse = CustomerResponse.builder()
                .customerID(1L)
                .customerName("John")
                .customerSurname("Doe")
                .customerMail("john.doe@example.com")
                .addressResponses(Arrays.asList(addressResponse1, addressResponse2))
                .build();

        AddressRequest addressRequest1 = AddressRequest.builder()
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .street("123 Main St")
                .build();

        AddressRequest addressRequest2 = AddressRequest.builder()
                .street("456 Park Ave")
                .city("Boston")
                .state("MA")
                .zipCode("02101")
                .build();

        customerRequest = CustomerRequest.builder()
                .customerName("John")
                .customerSurname("Doe")
                .customerMail("john.doe@example.com")
                .addressRequests(Arrays.asList(addressRequest1, addressRequest2))
                .build();
    }

    @Test
    void findCustomer_WhenCustomerExists_ReturnsCustomerResponse() {
        // Arrange
        String customerMail = "john.doe@example.com";
        when(customerRepository.findByCustomerMail(customerMail)).thenReturn(Optional.of(customer));
        when(customerMapper.customerResponseFromCustomer(customer)).thenReturn(customerResponse);

        // Act
        CustomerResponse result = customerService.findCustomer(customerMail);

        // Assert
        assertThat(result)
                .isNotNull()
                .satisfies(response -> {
                    assertThat(response.customerID()).isEqualTo(customerResponse.customerID());
                    assertThat(response.customerName()).isEqualTo(customerResponse.customerName());
                    assertThat(response.customerSurname()).isEqualTo(customerResponse.customerSurname());
                    assertThat(response.customerMail()).isEqualTo(customerResponse.customerMail());
                    assertThat(response.addressResponses())
                            .hasSize(customerResponse.addressResponses().size())
                            .containsExactlyElementsOf(customerResponse.addressResponses());
                });

        verify(customerRepository, times(1)).findByCustomerMail(customerMail);
        verify(customerMapper, times(1)).customerResponseFromCustomer(customer);
    }

    @Test
    void findCustomer_WhenCustomerDoesNotExist_ThrowsCustomerNotFoundException() {
        // Arrange
        String customerMail = "nonexistent@example.com";
        when(customerRepository.findByCustomerMail(customerMail)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> customerService.findCustomer(customerMail))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("Customer not found with given mail");

        verify(customerRepository, times(1)).findByCustomerMail(customerMail);
        verify(customerMapper, never()).customerResponseFromCustomer(any());
    }

    @Test
    void findCustomer_WhenCustomerHasNoAddresses_ReturnsCustomerResponseWithEmptyAddressList() {
        // Arrange
        String customerMail = "john.doe@example.com";
        Customer customerWithNoAddresses = Customer.builder()
                .id(1L)
                .customerName("John")
                .customerSurname("Doe")
                .customerMail(customerMail)
                .addresses(Collections.emptyList())
                .build();

        CustomerResponse customerResponseWithNoAddresses = CustomerResponse.builder()
                .customerID(1L)
                .customerName("John")
                .customerSurname("Doe")
                .customerMail(customerMail)
                .addressResponses(Collections.emptyList())
                .build();

        when(customerRepository.findByCustomerMail(customerMail)).thenReturn(Optional.of(customerWithNoAddresses));
        when(customerMapper.customerResponseFromCustomer(customerWithNoAddresses)).thenReturn(customerResponseWithNoAddresses);

        // Act
        CustomerResponse result = customerService.findCustomer(customerMail);

        // Assert
        assertThat(result)
                .isNotNull()
                .satisfies(response -> {
                    assertThat(response.addressResponses()).isEmpty();
                    assertThat(response.customerMail()).isEqualTo(customerMail);
                });

        verify(customerRepository, times(1)).findByCustomerMail(customerMail);
        verify(customerMapper, times(1)).customerResponseFromCustomer(customerWithNoAddresses);
    }

    @Test
    void createCustomer_WhenCustomerDoesNotExist_ReturnsCustomerResponse() {
        when(customerRepository.findByCustomerMail(customerRequest.customerMail())).thenReturn(Optional.empty());
        when(customerMapper.customerFromCustomerRequest(customerRequest)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.customerResponseFromCustomer(customer)).thenReturn(customerResponse);

        CustomerResponse actualResponse = customerService.createCustomer(customerRequest);

        assertThat(actualResponse)
                .isNotNull()
                .satisfies(response -> {
                    assertThat(response.customerID()).isEqualTo(customerResponse.customerID());
                    assertThat(response.customerName()).isEqualTo(customerResponse.customerName());
                    assertThat(response.customerSurname()).isEqualTo(customerResponse.customerSurname());
                    assertThat(response.customerMail()).isEqualTo(customerResponse.customerMail());
                    assertThat(response.addressResponses())
                            .hasSize(customerResponse.addressResponses().size())
                            .containsExactlyElementsOf(customerResponse.addressResponses());
                });

        verify(customerRepository, times(1)).findByCustomerMail(customerRequest.customerMail());
        verify(customerMapper, times(1)).customerFromCustomerRequest(customerRequest);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).customerResponseFromCustomer(customer);
    }

    @Test
    void createCustomer_WhenCustomerExists_ShouldThrowCustomerAlreadyExistException() {
        when(customerRepository.findByCustomerMail(customerRequest.customerMail())).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> customerService.createCustomer(customerRequest))
                .isInstanceOf(CustomerAlreadyExistException.class)
                .hasMessageContaining("Customer already exists with given mail");

        verify(customerRepository, times(1)).findByCustomerMail(customerRequest.customerMail());
        verify(customerMapper, never()).customerFromCustomerRequest(any());
        verify(customerRepository, never()).save(any());
        verify(customerMapper, never()).customerResponseFromCustomer(any());
    }
}
