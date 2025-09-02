package org.skillbox.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class RoomResponse {
    private Long id;
    private String name;
    private String description;
    private String number;
    private BigDecimal price;
    private Integer maxGuests;
    private List<LocalDate> unavailableDates;
    private Long hotelId;
    private String hotelName;
}