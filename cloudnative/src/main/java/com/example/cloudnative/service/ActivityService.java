package com.example.cloudnative.service;

import com.example.cloudnative.exception.ResourceNotFoundException;
import com.example.cloudnative.model.Activity;
import com.example.cloudnative.dto.ActivityResponse;
import com.example.cloudnative.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    // Constructor
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> getAllActivities() {
        return activityRepository.findByDeletedFalse();
    }

    public Activity getActivityById(UUID id) {
        return activityRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity with ID " + id + "not found!"));
    }

    public Activity createActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public Activity updateActivity(UUID id, Activity updatedActivity) {
        // Create activity instance
        Activity activity = getActivityById(id);
        activity.setName(updatedActivity.getName());
        activity.setDuration(updatedActivity.getDuration());
        activity.setPrice(updatedActivity.getPrice());
        return activityRepository.save(activity);
    }

    public void deleteActivity(UUID id) {
        Activity activity = getActivityById(id);
        activity.setDeleted(true); // Mark as deleted
        activityRepository.save(activity);
    }

    public ActivityResponse convertToDto(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getName(),
                activity.getDuration(),
                activity.getPrice(),
                activity.isDeleted()
        );
    }

}