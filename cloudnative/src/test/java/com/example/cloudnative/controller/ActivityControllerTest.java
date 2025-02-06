package com.example.cloudnative.controller;

import com.example.cloudnative.dto.ActivityResponse;
import com.example.cloudnative.model.Activity;
import com.example.cloudnative.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActivityController.class)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    @Test
    void testCreateActivity() throws Exception {
        UUID id = UUID.randomUUID();
        Activity activity = new Activity(id, "Training", "01:30", 20.0, false);
        ActivityResponse response = new ActivityResponse(id, "Training", "01:30", 20.0, false);

        when(activityService.createActivity(Mockito.any(Activity.class))).thenReturn(activity);
        when(activityService.convertToDto(activity)).thenReturn(response);

        mockMvc.perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Training\", \"duration\": \"01:30\", \"price\": 20.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Training"))
                .andExpect(jsonPath("$.duration").value("01:30"))
                .andExpect(jsonPath("$.price").value(20.0));
    }

    @Test
    void testGetActivityById() throws Exception {
        UUID id = UUID.randomUUID();
        Activity activity = new Activity(id, "Surfing", "02:00", 30.0, false);
        ActivityResponse response = new ActivityResponse(id, "Surfing", "02:00", 30.0, false);

        when(activityService.getActivityById(id)).thenReturn(activity);
        when(activityService.convertToDto(activity)).thenReturn(response);

        mockMvc.perform(get("/api/activities/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Surfing"))
                .andExpect(jsonPath("$.duration").value("02:00"))
                .andExpect(jsonPath("$.price").value(30.0));
    }
}
