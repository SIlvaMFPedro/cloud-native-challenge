package com.example.cloudnative.integration;

import com.example.cloudnative.model.Activity;
import com.example.cloudnative.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ActivityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ActivityRepository activityRepository;

    private UUID activityId;

    @BeforeEach
    void setup() {
        activityRepository.deleteAll();

        Activity activity = Activity.builder()
                .name("Training")
                .duration("01:30")
                .price(20.0)
                .deleted(false)
                .build();

        activity = activityRepository.save(activity);
        activityId = activity.getId();
    }

    @Test
    void testGetAllActivities() throws Exception {
        mockMvc.perform(get("/api/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Training")))
                .andExpect(jsonPath("$[0].duration", is("01:30")))
                .andExpect(jsonPath("$[0].price", is(20.0)));
    }

    @Test
    void testGetActivityById() throws Exception {
        mockMvc.perform(get("/api/activities/{id}", activityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Training"))
                .andExpect(jsonPath("$.duration").value("01:30"))
                .andExpect(jsonPath("$.price").value(20.0));
    }

    @Test
    void testCreateActivity() throws Exception {
        String activityJson = """
                {
                  "name": "Surfing",
                  "duration": "02:00",
                  "price": 30.0
                }
                """;

        mockMvc.perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(activityJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Surfing"))
                .andExpect(jsonPath("$.duration").value("02:00"))
                .andExpect(jsonPath("$.price").value(30.0));
    }

    @Test
    void testUpdateActivity() throws Exception {
        String updatedActivityJson = """
                {
                  "name": "Updated Training",
                  "duration": "02:15",
                  "price": 25.0
                }
                """;

        mockMvc.perform(put("/api/activities/{id}", activityId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedActivityJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Training"))
                .andExpect(jsonPath("$.duration").value("02:15"))
                .andExpect(jsonPath("$.price").value(25.0));
    }

    @Test
    void testDeleteActivity() throws Exception {
        mockMvc.perform(delete("/api/activities/{id}", activityId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/activities/{id}", activityId))
                .andExpect(status().isNotFound());
    }
}
