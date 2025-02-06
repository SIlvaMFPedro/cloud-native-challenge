package com.example.cloudnative.service;

import com.example.cloudnative.exception.ResourceNotFoundException;
import com.example.cloudnative.model.Activity;
import com.example.cloudnative.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    private Activity activity;
    private UUID activityId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        activityId = UUID.randomUUID();
        activity = new Activity(activityId, "Training", "01:30", 20.0, false);
    }

    @Test
    void testGetAllActivities() {
        when(activityRepository.findByDeletedFalse()).thenReturn(List.of(activity));
        List<Activity> activities = activityService.getAllActivities();
        assertFalse(activities.isEmpty());
        assertEquals(1, activities.size());
    }

    @Test
    void testGetActivityById_Success() {
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        Activity foundActivity = activityService.getActivityById(activityId);
        assertNotNull(foundActivity);
        assertEquals("Training", foundActivity.getName());
    }

    @Test
    void testGetActivityById_NotFound() {
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> activityService.getActivityById(activityId));
    }

    @Test
    void testCreateActivity() {
        when(activityRepository.save(any(Activity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Activity savedActivity = activityService.createActivity(activity);
        assertNotNull(savedActivity);
        assertEquals("Training", savedActivity.getName());
    }

    @Test
    void testDeleteActivity() {
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        activityService.deleteActivity(activityId);
        assertTrue(activity.isDeleted());
        verify(activityRepository, times(1)).save(activity);
    }
}
