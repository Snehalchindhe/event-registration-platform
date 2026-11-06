package com.codenova.eventregistration.service;

import com.codenova.eventregistration.dto.RegistrationAdminResponse;
import com.codenova.eventregistration.entity.Registration;
import com.codenova.eventregistration.repository.RegistrationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class AdminService {

    private final RegistrationRepository registrationRepository;

    public AdminService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @Transactional(readOnly = true)
    public List<RegistrationAdminResponse> getAllRegistrations(String search) {

        List<Registration> registrations =
                registrationRepository.findAllByOrderByRegisteredAtDesc();

        return registrations.stream()
                .filter(registration -> matchesSearch(registration, search))
                .map(registration -> new RegistrationAdminResponse(
                        registration.getId(),
                        registration.getEvent().getTitle(),
                        registration.getName(),
                        registration.getEmail(),
                        registration.getCollege(),
                        registration.getDomainInterests(),
                        registration.getStatus(),
                        registration.getRegisteredAt()
                ))
                .toList();
    }

    private boolean matchesSearch(Registration registration, String search) {

        if (search == null || search.isBlank()) {
            return true;
        }

        String keyword = search.toLowerCase(Locale.ROOT);

        return registration.getName().toLowerCase(Locale.ROOT).contains(keyword)
                || registration.getEmail().toLowerCase(Locale.ROOT).contains(keyword)
                || registration.getCollege().toLowerCase(Locale.ROOT).contains(keyword)
                || registration.getEvent().getTitle().toLowerCase(Locale.ROOT).contains(keyword);
    }
}
