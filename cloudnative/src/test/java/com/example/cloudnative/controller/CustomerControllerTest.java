package com.example.cloudnative.controller;

import com.example.cloudnative.model.Customer;
import com.example.cloudnative.service.CustomerService;
import com.example.cloudnative.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        customer = new Customer(
                UUID.randomUUID(), "John", "Doe",
                LocalDate.of(1990, 1, 1), 123456789L,
                "+1234567890", false
        );
    }

    @Test
    void shouldGetAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(customer));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void shouldGetCustomerById() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);

        mockMvc.perform(get("/api/customers/{id}", customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentCustomer() throws Exception {
        UUID randomId = UUID.randomUUID();
        
        when(customerService.getCustomerById(randomId))
                .thenThrow(new ResourceNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/customers/{id}", randomId))
                .andExpect(status().isNotFound());
    }

}
