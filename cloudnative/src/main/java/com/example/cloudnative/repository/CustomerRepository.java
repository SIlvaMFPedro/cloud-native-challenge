package com.example.cloudnative.repository;

import com.example.cloudnative.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    List<Customer> findByDeletedFalse();    // Fetch only non-deleted customers

    Optional<Customer> findByIdAndDeletedFalse(UUID id);   // Find active customer by ID

    Optional<Customer> findByFiscalNumberAndDeletedFalse(Long fiscalNumber);  // Find by unique fiscal number

    @Transactional
    @Modifying
    @Query("UPDATE Customer c SET c.deleted = true WHERE c.id = :id")
    void softDeleteById(@Param("id") UUID id);   // Soft delete customer
}
