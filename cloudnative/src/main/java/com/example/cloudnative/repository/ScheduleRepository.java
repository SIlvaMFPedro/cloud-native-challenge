package com.example.cloudnative.repository;

import com.example.cloudnative.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findByDeletedFalse();    // Fetch only non-deleted schedules
    Optional<Schedule> findByIdAndDeletedFalse(UUID id);
}