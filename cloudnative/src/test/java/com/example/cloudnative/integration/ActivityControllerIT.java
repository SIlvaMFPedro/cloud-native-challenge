package com.example.cloudnative;

import com.example.cloudnative.model.Activity;
import com.example.cloudnative.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActivityControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ActivityRepository activityRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/activities";
        activityRepository.deleteAll();
    }

    @Test
    void shouldCreateAndFetchActivity() {
        Activity activity = new Activity(UUID.randomUUID(), "Test Activity", "1:30",
                50.0, false);

        Activity created = restTemplate.postForObject(baseUrl, activity, Activity.class);
        assertNotNull(created);
    }
}
