package org.skillbox.event;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingEvent {
    private Long bookingId;
    private Long userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate eventDate;
}