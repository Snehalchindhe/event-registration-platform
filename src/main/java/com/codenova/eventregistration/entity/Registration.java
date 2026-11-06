package com.codenova.eventregistration.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "registrations",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_event_email",
            columnNames = {"event_id", "email"}
        )
    }
)
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String college;

    @Column(name = "domain_interests", nullable = false, length = 500)
    private String domainInterests;

    @Column(nullable = false)
    private String status = "CONFIRMED";

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    public Registration() {
    }

    @PrePersist
    public void onCreate() {
        if (registeredAt == null) {
            registeredAt = LocalDateTime.now();
        }

        if (status == null) {
            status = "CONFIRMED";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDomainInterests() {
        return domainInterests;
    }

    public void setDomainInterests(String domainInterests) {
        this.domainInterests = domainInterests;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }
}