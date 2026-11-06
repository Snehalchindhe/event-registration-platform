package com.codenova.eventregistration.dto;

import java.time.LocalDateTime;

public class EventResponse {

    private Long id;
    private String title;
    private String category;
    private String description;
    private LocalDateTime eventDate;
    private String venue;
    private int capacity;
    private long registeredCount;
    private int availableSeats;
    private String imageUrl;

    public EventResponse() {
    }

    public EventResponse(Long id, String title, String category,
                         String description, LocalDateTime eventDate,
                         String venue, int capacity, long registeredCount,
                         int availableSeats, String imageUrl) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.venue = venue;
        this.capacity = capacity;
        this.registeredCount = registeredCount;
        this.availableSeats = availableSeats;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public String getVenue() {
        return venue;
    }

    public int getCapacity() {
        return capacity;
    }

    public long getRegisteredCount() {
        return registeredCount;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
