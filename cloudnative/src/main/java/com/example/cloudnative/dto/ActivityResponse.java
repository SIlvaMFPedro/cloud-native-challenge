package com.example.cloudnative.dto;

import java.util.UUID;

public class ActivityResponse {

    private UUID id;
    private String name;
    private String duration;
    private Double price;
    private boolean deleted;

    public ActivityResponse(UUID id, String name, String duration, Double price, boolean deleted) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.price = price;
        this.deleted = deleted;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
