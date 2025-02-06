package com.example.cloudnative.service;

import com.example.cloudnative.exception.ResourceNotFoundException;
import com.example.cloudnative.model.Customer;
import com.example.cloudnative.dto.CustomerResponse;
import com.example.cloudnative.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
                .orElseThrow(() -> new ResourceNotFoundException("Customer with ID " + id + "not found!"));
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(UUID id, Customer updatedCustomer) {
        /*
        return customerRepository.findById(id)
            .map(customer -> {
                customer.setFirstName(updatedCustomer.getFirstName());
                customer.setLastName(updatedCustomer.getLastName());
                customer.setBirthdate(updatedCustomer.getBirthdate());
                customer.setFiscalNumber(updatedCustomer.getFiscalNumber());
                customer.setMobileNumber(updatedCustomer.getMobileNumber());
                return customerRepository.save(customer);
            }).orElseThrow(() -> new ResourceNotFoundException("Customer with ID " + id + "not found!"));
         */
        // Create customer instance
        Customer customer = getCustomerById(id);
        customer.setFirstName(updatedCustomer.getFirstName());
        customer.setLastName(updatedCustomer.getLastName());
        customer.setBirthdate(updatedCustomer.getBirthdate());
        customer.setFiscalNumber(updatedCustomer.getFiscalNumber());
        customer.setMobileNumber(updatedCustomer.getMobileNumber());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(UUID id) {
        Customer customer = getCustomerById(id);
        customer.setDeleted(true); // Mark as deleted instead of removing
        customerRepository.save(customer);
    }

    public CustomerResponse convertToDto(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getBirthdate(),
                customer.getFiscalNumber(),
                customer.getMobileNumber(),
                customer.isDeleted()
        );
    }

}
