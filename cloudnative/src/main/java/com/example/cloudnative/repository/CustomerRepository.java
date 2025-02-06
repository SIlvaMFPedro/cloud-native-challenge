package com.example.cloudnative.repository;

import com.example.cloudnative.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    List<Customer> findByDeletedFalse();    // Fetch only non-deleted customers
    Optional<Customer> findByIdAndDeletedFalse(UUID id);
}