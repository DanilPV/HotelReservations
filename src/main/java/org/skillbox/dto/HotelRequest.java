package org.skillbox.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class HotelRequest {
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 200)
    private String address;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal distanceFromCenter;
}

