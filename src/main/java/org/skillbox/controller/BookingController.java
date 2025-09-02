package org.skillbox.controller;

import org.skillbox.dto.BookingRequest;
import org.skillbox.dto.BookingResponse;
import org.skillbox.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.skillbox.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @bookingService.getBookingById(#id).userId == authentication.principal.id)")
    public BookingResponse getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
    public List<BookingResponse> getBookingsByUserId(@PathVariable Long userId) {
        return bookingService.getBookingsByUserId(userId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<BookingResponse> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return bookingService.getAllBookings(page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public BookingResponse createBooking(@Valid @RequestBody BookingRequest request) {
        return bookingService.createBooking(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @bookingService.getBookingById(#id).userId == authentication.principal.id)")
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }
}