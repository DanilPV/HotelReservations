package org.skillbox.kafka;

import org.skillbox.event.BookingEvent;
import org.skillbox.event.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String USER_REGISTRATION_TOPIC = "user-registration-events";
    private static final String BOOKING_EVENTS_TOPIC = "booking-events";

    public void sendUserRegistrationEvent(Long userId) {
        UserRegistrationEvent event = new UserRegistrationEvent();
        event.setUserId(userId);
        event.setRegistrationDate(LocalDate.now());

        kafkaTemplate.send(USER_REGISTRATION_TOPIC, event);
    }

    public void sendBookingEvent(Long bookingId, Long userId, LocalDate checkIn, LocalDate checkOut) {
        BookingEvent event = new BookingEvent();
        event.setBookingId(bookingId);
        event.setUserId(userId);
        event.setCheckInDate(checkIn);
        event.setCheckOutDate(checkOut);
        event.setEventDate(LocalDate.now());

        kafkaTemplate.send(BOOKING_EVENTS_TOPIC, event);
    }
}