package com.example.cloudnative.service;

import com.example.cloudnative.exception.ResourceNotFoundException;
import com.example.cloudnative.model.Schedule;
import com.example.cloudnative.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    private Schedule schedule;
    private UUID scheduleId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scheduleId = UUID.randomUUID();
        schedule = new Schedule(scheduleId, "Training Lesson", LocalDateTime.now().plusDays(1), false);
    }

    @Test
    void testGetAllSchedules() {
        when(scheduleRepository.findByDeletedFalse()).thenReturn(List.of(schedule));
        List<Schedule> schedules = scheduleService.getAllSchedules();
        assertFalse(schedules.isEmpty());
        verify(scheduleRepository, times(1)).findByDeletedFalse();
    }

    @Test
    void testGetScheduleById_Success() {
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        Schedule foundSchedule = scheduleService.getScheduleById(scheduleId);
        assertNotNull(foundSchedule);
        assertEquals("Training Lesson", foundSchedule.getDescription());
    }

    @Test
    void testGetScheduleById_NotFound() {
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> scheduleService.getScheduleById(scheduleId));
    }

    @Test
    void testDeleteSchedule() {
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        scheduleService.deleteSchedule(scheduleId);
        assertTrue(schedule.isDeleted());
        verify(scheduleRepository, times(1)).save(schedule);
    }
}
