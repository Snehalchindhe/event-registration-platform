package com.codenova.eventregistration.config;

import com.codenova.eventregistration.entity.Event;
import com.codenova.eventregistration.repository.EventRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedEvents(EventRepository eventRepository) {

        return args -> {

            if (eventRepository.count() > 0) {
                return;
            }

            Event event1 = new Event();
            event1.setTitle("Tech Innovation Summit 2026");
            event1.setCategory("Technology");
            event1.setDescription(
                    "Explore emerging technologies, artificial intelligence, " +
                    "cloud computing and the future of digital innovation."
            );
            event1.setEventDate(LocalDateTime.of(2026, 12, 15, 10, 0));
            event1.setVenue("Pune International Exhibition Centre");
            event1.setCapacity(200);
            event1.setImageUrl("tech");

            Event event2 = new Event();
            event2.setTitle("Web Development Bootcamp");
            event2.setCategory("Development");
            event2.setDescription(
                    "Learn modern web development concepts, frontend design, " +
                    "backend technologies and practical project building."
            );
            event2.setEventDate(LocalDateTime.of(2026, 12, 20, 9, 30));
            event2.setVenue("MIT Campus, Pune");
            event2.setCapacity(100);
            event2.setImageUrl("web");

            Event event3 = new Event();
            event3.setTitle("Cyber Security Awareness Workshop");
            event3.setCategory("Cyber Security");
            event3.setDescription(
                    "Understand online threats, ethical security practices, " +
                    "phishing attacks and ways to protect digital information."
            );
            event3.setEventDate(LocalDateTime.of(2027, 1, 10, 11, 0));
            event3.setVenue("Pune Knowledge Centre");
            event3.setCapacity(150);
            event3.setImageUrl("security");

            eventRepository.save(event1);
            eventRepository.save(event2);
            eventRepository.save(event3);

            System.out.println("Sample events inserted successfully!");
        };
    }
}
