package com.example.cloudnative.controller;

import com.example.cloudnative.model.Schedule;
import com.example.cloudnative.service.ScheduleService;
import com.example.cloudnative.dto.ScheduleResponse;
import com.example.cloudnative.dto.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {
        List<ScheduleResponse> schedules = scheduleService.getAllSchedules()
                .stream()
                .map(schedule -> new ScheduleResponse(
                    schedule.getId(),
                    schedule.getDescription(),
                    schedule.getDate(),
                    schedule.isDeleted()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(schedules);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable UUID id) {
        Schedule schedule = scheduleService.getScheduleById(id);
        ScheduleResponse response = new ScheduleResponse(
                schedule.getId(),
                schedule.getDescription(),
                schedule.getDate(),
                schedule.isDeleted()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody Schedule schedule) {
        Schedule createdSchedule = scheduleService.createSchedule(schedule);
        ScheduleResponse response = new ScheduleResponse(
                createdSchedule.getId(),
                createdSchedule.getDescription(),
                createdSchedule.getDate(),
                createdSchedule.isDeleted()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule(@PathVariable UUID id, @Valid @RequestBody Schedule updatedSchedule) {
        Schedule schedule = scheduleService.updateSchedule(id, updatedSchedule);
        ScheduleResponse response = new ScheduleResponse(
                schedule.getId(),
                schedule.getDescription(),
                schedule.getDate(),
                schedule.isDeleted()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteSchedule(@PathVariable UUID id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok(new GenericResponse("Schedule deleted successfully"));
    }
}
