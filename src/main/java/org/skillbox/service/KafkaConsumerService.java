package org.skillbox.service;

import org.skillbox.event.BookingEvent;
import org.skillbox.event.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skillbox.model.Statistic;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.skillbox.repository.mongo.StatisticRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final StatisticRepository statisticRepository;

    @KafkaListener(topics = "user-registration-events", groupId = "hotel-booking-group")
    public void consumeUserRegistrationEvent(UserRegistrationEvent event) {
        log.info("Received user registration event: {}", event);

        Statistic statistic = new Statistic();
        statistic.setEventType("USER_REGISTRATION");
        statistic.setUserId(event.getUserId());
        statistic.setEventData(event);
        statistic.setEventDate(event.getRegistrationDate());

        statisticRepository.save(statistic);
    }

    @KafkaListener(topics = "booking-events", groupId = "hotel-booking-group")
    public void consumeBookingEvent(BookingEvent event) {
        log.info("Received booking event: {}", event);

        Statistic statistic = new Statistic();
        statistic.setEventType("BOOKING");
        statistic.setUserId(event.getUserId());
        statistic.setEventData(event);
        statistic.setEventDate(event.getEventDate());

        statisticRepository.save(statistic);
    }
}
