package com.example.cloudnative.integration;

import com.example.cloudnative.model.Schedule;
import com.example.cloudnative.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ScheduleControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private UUID scheduleId;

    @BeforeEach
    void setup() {
        scheduleRepository.deleteAll();

        Schedule schedule = Schedule.builder()
                .description("Training Lesson")
                .date(LocalDateTime.now().plusDays(1))
                .deleted(false)
                .build();

        schedule = scheduleRepository.save(schedule);
        scheduleId = schedule.getId();
    }

    @Test
    void testGetAllSchedules() throws Exception {
        mockMvc.perform(get("/api/schedules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is("Training Lesson")));
    }

    @Test
    void testCreateSchedule() throws Exception {
        String scheduleJson = """
                {
                  "description": "Surfing Class",
                  "date": "2025-06-15T14:00:00"
                }
                """;

        mockMvc.perform(post("/api/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Surfing Class")));
    }

    @Test
    void testDeleteSchedule() throws Exception {
        mockMvc.perform(delete("/api/schedules/{id}", scheduleId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/schedules/{id}", scheduleId))
                .andExpect(status().isNotFound());
    }
}
