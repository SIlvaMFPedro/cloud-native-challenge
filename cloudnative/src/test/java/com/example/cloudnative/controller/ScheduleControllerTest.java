package com.example.cloudnative.controller;

import com.example.cloudnative.dto.ScheduleResponse;
import com.example.cloudnative.model.Schedule;
import com.example.cloudnative.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @Test
    void testCreateSchedule() throws Exception {
        UUID id = UUID.randomUUID();
        Schedule schedule = new Schedule(id, "Morning Jog", LocalDateTime.of(2025, 6, 15, 9, 0), false);
        ScheduleResponse response = new ScheduleResponse(id, "Morning Jog", LocalDateTime.of(2025, 6, 15, 9, 0), false);

        when(scheduleService.createSchedule(Mockito.any(Schedule.class))).thenReturn(schedule);
        when(scheduleService.convertToDto(schedule)).thenReturn(response);

        mockMvc.perform(post("/api/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "description": "Morning Jog",
                                    "date": "2025-06-15T09:00:00"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Morning Jog"));
    }
}
