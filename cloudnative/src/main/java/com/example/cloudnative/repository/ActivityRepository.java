package com.example.cloudnative.repository;

import com.example.cloudnative.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    List<Activity> findByDeletedFalse();                   // Fetch only non-deleted activities
    Optional<Activity> findByIdAndDeletedFalse(UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE Activity c SET c.deleted = true WHERE c.id = :id")
    void softDeleteById(@Param("id") UUID id);   // Soft delete activity
}