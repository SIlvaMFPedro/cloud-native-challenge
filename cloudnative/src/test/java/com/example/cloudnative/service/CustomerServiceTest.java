package com.example.cloudnative.service;

import com.example.cloudnative.exception.ResourceNotFoundException;
import com.example.cloudnative.model.Customer;
import com.example.cloudnative.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer(
                UUID.randomUUID(), "John", "Doe",
                LocalDate.of(1990, 1, 1), 123456789L,
                "+1234567890", false
        );
    }

    @Test
    void shouldReturnAllCustomers() {
        when(customerRepository.findByDeletedFalse()).thenReturn(List.of(customer));

        List<Customer> customers = customerService.getAllCustomers();
        
        assertEquals(1, customers.size());
        verify(customerRepository, times(1)).findByDeletedFalse();
    }

    @Test
    void shouldReturnCustomerById() {
        when(customerRepository.findByIdAndDeletedFalse(customer.getId())).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.getCustomerById(customer.getId());

        assertNotNull(foundCustomer);
        assertEquals(customer.getId(), foundCustomer.getId());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        UUID randomId = UUID.randomUUID();
        when(customerRepository.findByIdAndDeletedFalse(randomId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(randomId));
    }

    @Test
    void shouldCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer createdCustomer = customerService.createCustomer(customer);

        assertNotNull(createdCustomer);
        assertEquals("John", createdCustomer.getFirstName());
    }

    @Test
    void shouldDeleteCustomer() {
        when(customerRepository.existsById(customer.getId())).thenReturn(true);
        doNothing().when(customerRepository).softDeleteById(customer.getId());

        customerService.deleteCustomer(customer.getId());

        verify(customerRepository, times(1)).softDeleteById(customer.getId());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentCustomer() {
        UUID randomId = UUID.randomUUID();
        when(customerRepository.existsById(randomId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(randomId));
    }
}
