package com.example.cloudnative;

import com.example.cloudnative.model.Customer;
import com.example.cloudnative.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/customers";
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateAndFetchCustomer() {
        Customer customer = new Customer(UUID.randomUUID(), "John", "Doe",
                LocalDate.of(1990, 1, 1), 123456789L, "+1234567890", false);

        Customer created = restTemplate.postForObject(baseUrl, customer, Customer.class);
        assertNotNull(created);
    }
}
