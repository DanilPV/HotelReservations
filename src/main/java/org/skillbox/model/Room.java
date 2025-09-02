package org.skillbox.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @Size(max = 500)
    private String description;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, unique = true)
    private String number;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false)
    private BigDecimal price;

    @Min(1)
    @Max(10)
    @Column(name = "max_guests", nullable = false)
    private Integer maxGuests;

    @ElementCollection
    @CollectionTable(name = "room_unavailable_dates", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "unavailable_date")
    private List<LocalDate> unavailableDates;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
}