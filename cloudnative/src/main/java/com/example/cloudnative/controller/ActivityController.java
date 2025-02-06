package com.example.cloudnative.controller;

import com.example.cloudnative.model.Activity;
import com.example.cloudnative.service.ActivityService;
import com.example.cloudnative.dto.ActivityResponse;
import com.example.cloudnative.dto.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    /**
     * Constructor
     */
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public List<Activity> getAllActivities() {
        return activityService.getAllActivities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> getActivity(@PathVariable UUID id) {
        Activity activity = activityService.getActivityById(id);
        ActivityResponse response = new ActivityResponse(
                activity.getId(),
                activity.getName(),
                activity.getDuration(),
                activity.getPrice(),
                activity.isDeleted()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(@Valid @RequestBody Activity activity) {
        Activity createdActivity = activityService.createActivity(activity);
        ActivityResponse response = new ActivityResponse(
                createdActivity.getId(),
                createdActivity.getName(),
                createdActivity.getDuration(),
                createdActivity.getPrice(),
                createdActivity.isDeleted()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponse> updateActivity(@PathVariable UUID id, @Valid @RequestBody Activity updatedActivity) {
        Activity activity = activityService.updateActivity(id, updatedActivity);
        ActivityResponse response = new ActivityResponse(
                activity.getId(),
                activity.getName(),
                activity.getDuration(),
                activity.getPrice(),
                activity.isDeleted()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteActivity(@PathVariable UUID id) {
        activityService.deleteActivity(id);
        // return ResponseEntity.ok(new GenericResponse("Activity deleted successfully"));
        return ResponseEntity.noContent().build();  // Return a 204 instead of a 200
    }
}
