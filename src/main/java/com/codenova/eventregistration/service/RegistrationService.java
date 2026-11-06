package com.codenova.eventregistration.service;

import com.codenova.eventregistration.dto.RegistrationRequest;
import com.codenova.eventregistration.dto.RegistrationResponse;
import com.codenova.eventregistration.entity.Event;
import com.codenova.eventregistration.entity.Registration;
import com.codenova.eventregistration.repository.EventRepository;
import com.codenova.eventregistration.repository.RegistrationRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public RegistrationService(EventRepository eventRepository,
                               RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public RegistrationResponse registerParticipant(
            Long eventId, RegistrationRequest request) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new RuntimeException("Event not found with ID: " + eventId));

        if (registrationRepository.existsByEventIdAndEmail(
                eventId, request.getEmail())) {
            throw new IllegalStateException(
                    "This email is already registered for this event.");
        }

        long currentCount = registrationRepository.countByEventId(eventId);

        if (currentCount >= event.getCapacity()) {
            throw new IllegalStateException(
                    "Sorry, this event is fully booked.");
        }

        Registration registration = new Registration();
        registration.setEvent(event);
        registration.setName(request.getName());
        registration.setEmail(request.getEmail().trim().toLowerCase());
        registration.setCollege(request.getCollege());
        registration.setDomainInterests(request.getDomainInterests());
        registration.setStatus("CONFIRMED");

        try {
            Registration saved =
                    registrationRepository.saveAndFlush(registration);

            long updatedCount = registrationRepository.countByEventId(eventId);
            int availableSeats = Math.max(
                    0, event.getCapacity() - (int) updatedCount
            );

            return new RegistrationResponse(
                    "Registration successful! Welcome to the event.",
                    saved.getId(),
                    updatedCount,
                    availableSeats
            );

        } catch (DataIntegrityViolationException exception) {
            throw new IllegalStateException(
                    "This email is already registered for this event.");
        }
    }
}
