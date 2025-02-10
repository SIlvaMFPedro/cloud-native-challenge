package com.example.cloudnative.repository;

import com.example.cloudnative.model.Schedule;
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
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findByDeletedFalse();    // Fetch only non-deleted schedules
    Optional<Schedule> findByIdAndDeletedFalse(UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE Schedule c SET c.deleted = true WHERE c.id = :id")
    void softDeleteById(@Param("id") UUID id);   // Soft delete schedule
}