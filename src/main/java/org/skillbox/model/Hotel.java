package org.skillbox.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String city;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false)
    private String address;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "distance_from_center", nullable = false)
    private BigDecimal distanceFromCenter;

    @DecimalMin("0.0")
    @DecimalMax("5.0")
    @Column
    private Double rating;

    @Min(0)
    @Column(name = "rating_count")
    private Integer ratingCount;
}