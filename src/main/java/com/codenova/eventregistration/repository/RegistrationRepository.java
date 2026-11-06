package com.codenova.eventregistration.repository;

import com.codenova.eventregistration.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByEventIdAndEmail(Long eventId, String email);

    long countByEventId(Long eventId);

    List<Registration> findByEventId(Long eventId);

    Optional<Registration> findByEventIdAndEmail(Long eventId, String email);

    List<Registration> findAllByOrderByRegisteredAtDesc();
}