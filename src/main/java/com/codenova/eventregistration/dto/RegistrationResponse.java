package com.codenova.eventregistration.dto;

public class RegistrationResponse {

    private String message;
    private Long registrationId;
    private long registeredCount;
    private int availableSeats;

    public RegistrationResponse() {
    }

    public RegistrationResponse(String message, Long registrationId,
                                long registeredCount, int availableSeats) {
        this.message = message;
        this.registrationId = registrationId;
        this.registeredCount = registeredCount;
        this.availableSeats = availableSeats;
    }

    public String getMessage() {
        return message;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public long getRegisteredCount() {
        return registeredCount;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }
}
