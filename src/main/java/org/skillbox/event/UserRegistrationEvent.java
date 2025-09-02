package org.skillbox.event;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegistrationEvent {
    private Long userId;
    private LocalDate registrationDate;
}