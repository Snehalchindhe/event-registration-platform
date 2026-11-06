package com.codenova.eventregistration.service;

import com.codenova.eventregistration.dto.EventResponse;
import com.codenova.eventregistration.entity.Event;
import com.codenova.eventregistration.repository.EventRepository;
import com.codenova.eventregistration.repository.RegistrationRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public EventService(EventRepository eventRepository,
                        RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    public List<EventResponse> getAllEvents() {

        return eventRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public EventResponse getEventById(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Event not found with ID: " + id));

        return convertToResponse(event);
    }

    private EventResponse convertToResponse(Event event) {

        long registeredCount =
                registrationRepository.countByEventId(event.getId());

        int availableSeats = Math.max(
                0, event.getCapacity() - (int) registeredCount
        );

        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getCategory(),
                event.getDescription(),
                event.getEventDate(),
                event.getVenue(),
                event.getCapacity(),
                registeredCount,
                availableSeats,
                event.getImageUrl()
        );
    }
}
