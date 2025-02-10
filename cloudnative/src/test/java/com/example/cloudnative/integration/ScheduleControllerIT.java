package com.example.cloudnative;

import com.example.cloudnative.model.Schedule;
import com.example.cloudnative.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScheduleControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/schedules";
        scheduleRepository.deleteAll();
    }

    @Test
    void shouldCreateAndFetchSchedule() {
        Schedule schedule = new Schedule(UUID.randomUUID(), "Test Schedule",
                LocalDate.of(2025, 2, 23).atStartOfDay(), false);

        Schedule created = restTemplate.postForObject(baseUrl, schedule, Schedule.class);
        assertNotNull(created);
    }
}
