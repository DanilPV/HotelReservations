package org.skillbox.specification;

import org.skillbox.model.Booking;
import org.skillbox.model.Room;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomSpecification {

    public static Specification<Room> withFilters(
            Long id, String name, BigDecimal minPrice, BigDecimal maxPrice,
            Integer maxGuests, LocalDate checkIn, LocalDate checkOut, Long hotelId) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), id));
            }

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }

            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"), maxPrice));
            }

            if (maxGuests != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("maxGuests"), maxGuests));
            }

            if (hotelId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("hotel").get("id"), hotelId));
            }

            if (checkIn != null && checkOut != null) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Booking> bookingRoot = subquery.from(Booking.class);

                subquery.select(bookingRoot.get("room").get("id"));
                subquery.where(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(bookingRoot.get("room").get("id"), root.get("id")),
                                criteriaBuilder.lessThanOrEqualTo(bookingRoot.get("checkInDate"), checkOut),
                                criteriaBuilder.greaterThanOrEqualTo(bookingRoot.get("checkOutDate"), checkIn)
                        )
                );

                predicates.add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}