package org.skillbox.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class HotelResponse {
    private Long id;
    private String name;
    private String title;
    private String city;
    private String address;
    private BigDecimal distanceFromCenter;
    private Double rating;
    private Integer ratingCount;
}
