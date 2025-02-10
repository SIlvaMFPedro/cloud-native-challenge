package com.example.cloudnative.service;

import com.example.cloudnative.exception.ResourceNotFoundException;
import com.example.cloudnative.model.Activity;
import com.example.cloudnative.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    private Activity activity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        activity = new Activity(
                UUID.randomUUID(), "Test Activity", "1:30",
                50.0, 
                false
        );
    }

    @Test
    void shouldReturnAllActivities() {
        when(activityRepository.findByDeletedFalse()).thenReturn(List.of(activity));

        List<Activity> activities = activityService.getAllActivities();
        
        assertEquals(1, activities.size());
        verify(activityRepository, times(1)).findByDeletedFalse();
    }

    @Test
    void shouldReturnActivityById() {
        when(activityRepository.findByIdAndDeletedFalse(activity.getId())).thenReturn(Optional.of(activity));

        Activity foundActivity = activityService.getActivityById(activity.getId());

        assertNotNull(foundActivity);
        assertEquals(activity.getId(), foundActivity.getId());
    }

    @Test
    void shouldThrowExceptionWhenActivityNotFound() {
        UUID randomId = UUID.randomUUID();
        when(activityRepository.findByIdAndDeletedFalse(randomId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> activityService.getActivityById(randomId));
    }

    @Test
    void shouldCreateActivity() {
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);

        Activity createdActivity = activityService.createActivity(activity);

        assertNotNull(createdActivity);
        assertEquals("Test Activity", createdActivity.getName());
    }

    @Test
    void shouldDeleteActivity() {
        when(activityRepository.existsById(activity.getId())).thenReturn(true);
        doNothing().when(activityRepository).softDeleteById(activity.getId());

        activityService.deleteActivity(activity.getId());

        verify(activityRepository, times(1)).softDeleteById(activity.getId());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentActivity() {
        UUID randomId = UUID.randomUUID();
        when(activityRepository.existsById(randomId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> activityService.deleteActivity(randomId));
    }
}
