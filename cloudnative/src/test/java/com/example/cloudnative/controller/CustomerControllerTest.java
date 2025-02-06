package com.example.cloudnative.controller;

import com.example.cloudnative.dto.CustomerResponse;
import com.example.cloudnative.model.Customer;
import com.example.cloudnative.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;  // ✅ CORRECT IMPORT

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;  // ✅ CORRECT IMPORT
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void testCreateCustomer() throws Exception {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "John", "Doe", LocalDate.of(1990, 5, 20), 123456789L, "+1234567890", false);
        CustomerResponse response = new CustomerResponse(id, "John", "Doe", LocalDate.of(1990, 5, 20), 123456789L, "+1234567890", false);

        when(customerService.createCustomer(Mockito.any(Customer.class))).thenReturn(customer);
        when(customerService.convertToDto(customer)).thenReturn(response);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "John",
                                    "lastName": "Doe",
                                    "birthdate": "1990-05-20",
                                    "fiscalNumber": 123456789,
                                    "mobileNumber": "+1234567890"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }
}
