package com.codenova.eventregistration.controller;

import com.codenova.eventregistration.dto.RegistrationRequest;
import com.codenova.eventregistration.dto.RegistrationResponse;
import com.codenova.eventregistration.service.RegistrationService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/{eventId}/registrations")
    public RegistrationResponse registerParticipant(
            @PathVariable Long eventId,
            @Valid @RequestBody RegistrationRequest request) {

        return registrationService.registerParticipant(eventId, request);
    }
}
