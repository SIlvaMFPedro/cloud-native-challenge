package com.example.cloudnative.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ScheduleResponse {

    private UUID id;
    private String description;
    private LocalDateTime date;
    private boolean deleted;

    public ScheduleResponse(UUID id, String description, LocalDateTime date, boolean deleted) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.deleted = deleted;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
