package com.codenova.eventregistration.dto;

import java.time.LocalDateTime;

public class RegistrationAdminResponse {

    private Long id;
    private String eventTitle;
    private String name;
    private String email;
    private String college;
    private String domainInterests;
    private String status;
    private LocalDateTime registeredAt;

    public RegistrationAdminResponse(Long id, String eventTitle,
                                     String name, String email,
                                     String college, String domainInterests,
                                     String status, LocalDateTime registeredAt) {
        this.id = id;
        this.eventTitle = eventTitle;
        this.name = name;
        this.email = email;
        this.college = college;
        this.domainInterests = domainInterests;
        this.status = status;
        this.registeredAt = registeredAt;
    }

    public Long getId() {
        return id;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCollege() {
        return college;
    }

    public String getDomainInterests() {
        return domainInterests;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }
}
