package org.skillbox.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingResponse {
    private Long id;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate checkInDate;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate checkOutDate;
    private Long roomId;
    private String roomNumber;
    private Long hotelId;
    private String hotelName;
    private Long userId;
    private String username;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate createdAt;
}