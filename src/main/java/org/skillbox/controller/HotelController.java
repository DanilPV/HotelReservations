package org.skillbox.controller;

import org.skillbox.dto.HotelRatingRequest;
import org.skillbox.dto.HotelRequest;
import org.skillbox.dto.HotelResponse;
import org.skillbox.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.skillbox.service.HotelService;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/{id}")
    public HotelResponse getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    @GetMapping
    public List<HotelResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/filter")
    public PageResponse<HotelResponse> getHotelsWithFilter(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double minDistance,
            @RequestParam(required = false) Double maxDistance,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Integer minRatingCount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return hotelService.getHotelsWithFilter(
                id, name, title, city, address, minDistance, maxDistance,
                minRating, minRatingCount, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public HotelResponse createHotel(@Valid @RequestBody HotelRequest request) {
        return hotelService.createHotel(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public HotelResponse updateHotel(
            @PathVariable Long id, @Valid @RequestBody HotelRequest request) {
        return hotelService.updateHotel(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
    }

    @PostMapping("/{id}/rate")
    @PreAuthorize("isAuthenticated()")
    public HotelResponse rateHotel(
            @PathVariable Long id, @Valid @RequestBody HotelRatingRequest request) {
        return hotelService.rateHotel(id, request.getRating());
    }
}