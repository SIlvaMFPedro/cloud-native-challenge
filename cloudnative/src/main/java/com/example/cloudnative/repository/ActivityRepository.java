package com.example.cloudnative.repository;

import com.example.cloudnative.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    List<Activity> findByDeletedFalse();                   // Fetch only non-deleted activities
    Optional<Activity> findByIdAndDeletedFalse(UUID id);
}