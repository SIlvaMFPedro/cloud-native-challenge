package com.example.cloudnative.controller;

import com.example.cloudnative.dto.CustomerResponse;
import com.example.cloudnative.dto.GenericResponse;
import com.example.cloudnative.model.Customer;
import com.example.cloudnative.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Constructor
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        CustomerResponse response = new CustomerResponse(
                createdCustomer.getId(),
                createdCustomer.getFirstName(),
                createdCustomer.getLastName(),
                createdCustomer.getBirthdate(),
                createdCustomer.getFiscalNumber(),
                createdCustomer.getMobileNumber(),
                createdCustomer.isDeleted()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable UUID id, @Valid @RequestBody Customer updatedCustomer) {
        Customer customer = customerService.updateCustomer(id, updatedCustomer);
        CustomerResponse response = new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getBirthdate(),
                customer.getFiscalNumber(),
                customer.getMobileNumber(),
                customer.isDeleted()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable UUID id) {
        Customer customer = customerService.getCustomerById(id);
        CustomerResponse response = new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getBirthdate(),
                customer.getFiscalNumber(),
                customer.getMobileNumber(),
                customer.isDeleted()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(new GenericResponse("Customer deleted successfully"));
    }
}
