package com.example.cloudnative.service;

import com.example.cloudnative.dto.CustomerResponse;
import com.example.cloudnative.exception.ResourceNotFoundException;
import com.example.cloudnative.model.Customer;
import com.example.cloudnative.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    // Constructor
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findByDeletedFalse();
    }

    public Customer getCustomerById(UUID id) {
        return customerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with ID " + id + " not found!"));
    }

    public Customer createCustomer(Customer customer) {
        // Check if the fiscal number is already in use
        Optional<Customer> existingCustomer = customerRepository.findByFiscalNumberAndDeletedFalse(customer.getFiscalNumber());
        if (existingCustomer.isPresent()) {
            throw new IllegalArgumentException("A customer with this fiscal number already exists!");
        }
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(UUID id, Customer updatedCustomer) {
        Customer customer = getCustomerById(id);

        // Prevent duplicate fiscal numbers when updating
        Optional<Customer> existingCustomer = customerRepository.findByFiscalNumberAndDeletedFalse(updatedCustomer.getFiscalNumber());
        if (existingCustomer.isPresent() && !existingCustomer.get().getId().equals(id)) {
            throw new IllegalArgumentException("A customer with this fiscal number already exists!");
        }

        customer.setFirstName(updatedCustomer.getFirstName());
        customer.setLastName(updatedCustomer.getLastName());
        customer.setBirthdate(updatedCustomer.getBirthdate());
        customer.setFiscalNumber(updatedCustomer.getFiscalNumber());
        customer.setMobileNumber(updatedCustomer.getMobileNumber());

        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer with ID " + id + " not found!");
        }
        customerRepository.softDeleteById(id); // More efficient than fetching before updating
    }
}
