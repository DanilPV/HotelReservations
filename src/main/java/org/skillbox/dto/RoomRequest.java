package org.skillbox.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class RoomRequest {
    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NotBlank
    @Size(max = 20)
    private String number;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @Min(1)
    @Max(10)
    private Integer maxGuests;

    @NotNull
    private Long hotelId;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private List<LocalDate> unavailableDates;
}