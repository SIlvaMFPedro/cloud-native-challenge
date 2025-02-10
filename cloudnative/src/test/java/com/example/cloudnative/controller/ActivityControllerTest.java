package com.example.cloudnative.controller;

import com.example.cloudnative.model.Activity;
import com.example.cloudnative.service.ActivityService;
import com.example.cloudnative.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ActivityControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private ActivityController activityController;

    private Activity activity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(activityController).build();

        activity = new Activity(
                UUID.randomUUID(), "Test Activity", "1:30",
                50.0, false
        );
    }

    @Test
    void shouldGetAllActivities() throws Exception {
        when(activityService.getAllActivities()).thenReturn(List.of(activity));

        mockMvc.perform(get("/api/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void shouldGetActivityById() throws Exception {
        when(activityService.getActivityById(activity.getId())).thenReturn(activity);

        mockMvc.perform(get("/api/activities/{id}", activity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Activity"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentActivity() throws Exception {
        UUID randomId = UUID.randomUUID();
        
        when(activityService.getActivityById(randomId))
                .thenThrow(new ResourceNotFoundException("Activity not found"));

        mockMvc.perform(get("/api/activities/{id}", randomId))
                .andExpect(status().isNotFound());
    }

}
