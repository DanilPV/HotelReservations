package org.skillbox.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    @NotNull
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate checkInDate;

    @NotNull
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate checkOutDate;

    @NotNull
    private Long roomId;

    @NotNull
    private Long userId;
}