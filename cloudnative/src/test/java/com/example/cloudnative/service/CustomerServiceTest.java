package com.example.cloudnative.service;

import com.example.cloudnative.exception.ResourceNotFoundException;
import com.example.cloudnative.model.Customer;
import com.example.cloudnative.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerId = UUID.randomUUID();
        customer = new Customer(customerId, "John", "Doe", LocalDate.of(1990, 5, 20), 123456789L, "+1234567890", false);
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findByDeletedFalse()).thenReturn(List.of(customer));
        List<Customer> customers = customerService.getAllCustomers();
        assertFalse(customers.isEmpty());
        verify(customerRepository, times(1)).findByDeletedFalse();
    }

    @Test
    void testGetCustomerById_Success() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Customer foundCustomer = customerService.getCustomerById(customerId);
        assertNotNull(foundCustomer);
        assertEquals("John", foundCustomer.getFirstName());
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(customerId));
    }

    @Test
    void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Customer savedCustomer = customerService.createCustomer(customer);
        assertNotNull(savedCustomer);
        assertEquals("John", savedCustomer.getFirstName());
    }

    @Test
    void testDeleteCustomer() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        customerService.deleteCustomer(customerId);
        assertTrue(customer.isDeleted());
        verify(customerRepository, times(1)).save(customer);
    }
}
