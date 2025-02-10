package com.example.cloudnative.controller;

import com.example.cloudnative.model.Schedule;
import com.example.cloudnative.service.ScheduleService;
import com.example.cloudnative.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ScheduleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    private Schedule schedule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();

        schedule = new Schedule(
                UUID.randomUUID(), 
                "Test Schedule",
                LocalDate.of(2025, 2, 23).atStartOfDay(),  
                false
        );
    }

    @Test
    void shouldGetAllSchedules() throws Exception {
        when(scheduleService.getAllSchedules()).thenReturn(List.of(schedule));

        mockMvc.perform(get("/api/schedules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void shouldGetScheduleById() throws Exception {
        when(scheduleService.getScheduleById(schedule.getId())).thenReturn(schedule);

        mockMvc.perform(get("/api/schedules/{id}", schedule.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test Schedule"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentSchedule() throws Exception {
        UUID randomId = UUID.randomUUID();
        
        when(scheduleService.getScheduleById(randomId))
                .thenThrow(new ResourceNotFoundException("Schedule not found"));

        mockMvc.perform(get("/api/schedules/{id}", randomId))
                .andExpect(status().isNotFound());
    }

}
