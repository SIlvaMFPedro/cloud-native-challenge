package com.example.cloudnative.service;

import com.example.cloudnative.exception.ResourceNotFoundException;
import com.example.cloudnative.model.Schedule;
import com.example.cloudnative.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    private Schedule schedule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        schedule = new Schedule(
                UUID.randomUUID(),
                "Test Schedule",
                LocalDate.of(2025, 2, 23).atStartOfDay(),
                false
        );
    }

    @Test
    void shouldReturnAllSchedules() {
        when(scheduleRepository.findByDeletedFalse()).thenReturn(List.of(schedule));

        List<Schedule> schedules = scheduleService.getAllSchedules();
        
        assertEquals(1, schedules.size());
        verify(scheduleRepository, times(1)).findByDeletedFalse();
    }

    @Test
    void shouldReturnScheduleById() {
        when(scheduleRepository.findByIdAndDeletedFalse(schedule.getId())).thenReturn(Optional.of(schedule));

        Schedule foundSchedule = scheduleService.getScheduleById(schedule.getId());

        assertNotNull(foundSchedule);
        assertEquals(schedule.getId(), foundSchedule.getId());
    }

    @Test
    void shouldThrowExceptionWhenScheduleNotFound() {
        UUID randomId = UUID.randomUUID();
        when(scheduleRepository.findByIdAndDeletedFalse(randomId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> scheduleService.getScheduleById(randomId));
    }

    @Test
    void shouldCreateSchedule() {
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Schedule createdSchedule = scheduleService.createSchedule(schedule);

        assertNotNull(createdSchedule);
        assertEquals("Test Schedule", createdSchedule.getDescription());
    }

    @Test
    void shouldDeleteSchedule() {
        when(scheduleRepository.existsById(schedule.getId())).thenReturn(true);
        doNothing().when(scheduleRepository).softDeleteById(schedule.getId());

        scheduleService.deleteSchedule(schedule.getId());

        verify(scheduleRepository, times(1)).softDeleteById(schedule.getId());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentSchedule() {
        UUID randomId = UUID.randomUUID();
        when(scheduleRepository.existsById(randomId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> scheduleService.deleteSchedule(randomId));
    }
}
