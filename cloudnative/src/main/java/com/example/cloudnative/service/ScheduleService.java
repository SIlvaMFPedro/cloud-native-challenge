package com.example.cloudnative.service;

import com.example.cloudnative.exception.ResourceNotFoundException;
import com.example.cloudnative.model.Schedule;
import com.example.cloudnative.dto.ScheduleResponse;
import com.example.cloudnative.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findByDeletedFalse();
    }

    public Schedule getScheduleById(UUID id) {
        return scheduleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule with ID " + id + "not found!"));
    }

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(UUID id, Schedule updatedSchedule) {
        // Create schedule instance
        Schedule schedule = getScheduleById(id);
        schedule.setDescription(updatedSchedule.getDescription());
        schedule.setDate(updatedSchedule.getDate());
        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(UUID id) {
        Schedule schedule = getScheduleById(id);
        schedule.setDeleted(true); // Mark as deleted
        scheduleRepository.save(schedule);
    }

    public ScheduleResponse convertToDto(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getDescription(),
                schedule.getDate(),
                schedule.isDeleted()
        );
    }

}
