package org.skillbox.service;

import org.skillbox.dto.HotelRequest;
import org.skillbox.dto.HotelResponse;
import org.skillbox.dto.PageResponse;
import org.skillbox.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.skillbox.mapper.HotelMapper;
import org.skillbox.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.skillbox.repository.jpa.HotelRepository;
import org.skillbox.specification.HotelSpecification;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public HotelResponse getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Отель не найден с id: " + id));
        return hotelMapper.toResponse(hotel);
    }

    public List<HotelResponse> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotelMapper.toResponseList(hotels);
    }

    public PageResponse<HotelResponse> getHotelsWithFilter(
            Long id, String name, String title, String city, String address,
            Double minDistance, Double maxDistance, Double minRating,
            Integer minRatingCount, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Specification<Hotel> spec = HotelSpecification.withFilters(
                id, name, title, city, address, minDistance, maxDistance,
                minRating, minRatingCount);

        Page<Hotel> hotelPage = hotelRepository.findAll(spec, pageable);

        PageResponse<HotelResponse> response = new PageResponse<>();
        response.setContent(hotelMapper.toResponseList(hotelPage.getContent()));
        response.setPage(hotelPage.getNumber());
        response.setSize(hotelPage.getSize());
        response.setTotalElements(hotelPage.getTotalElements());
        response.setTotalPages(hotelPage.getTotalPages());
        response.setLast(hotelPage.isLast());

        return response;
    }

    @Transactional
    public HotelResponse createHotel(HotelRequest request) {
        Hotel hotel = hotelMapper.toEntity(request);
        hotel.setRating(0.0);
        hotel.setRatingCount(0);
        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelMapper.toResponse(savedHotel);
    }

    @Transactional
    public HotelResponse updateHotel(Long id, HotelRequest request) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Отель не найден с id: " + id));

        hotelMapper.updateEntityFromRequest(request, hotel);
        Hotel updatedHotel = hotelRepository.save(hotel);
        return hotelMapper.toResponse(updatedHotel);
    }

    @Transactional
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new NotFoundException("Отель не найден с id: " + id);
        }
        hotelRepository.deleteById(id);
    }

    @Transactional
    public HotelResponse rateHotel(Long id, Integer rating) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Отель не найден с id: " + id));

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Оценка должна быть в диапазоне от 1 до 5");
        }

        double currentRating = hotel.getRating() != null ? hotel.getRating() : 0.0;
        int currentCount = hotel.getRatingCount() != null ? hotel.getRatingCount() : 0;

        double totalRating = currentRating * currentCount;
        totalRating = totalRating - currentRating + rating;

        int newCount = currentCount + 1;
        double newRating = Math.round((totalRating / newCount) * 10.0) / 10.0;

        hotel.setRating(newRating);
        hotel.setRatingCount(newCount);

        Hotel updatedHotel = hotelRepository.save(hotel);
        return hotelMapper.toResponse(updatedHotel);
    }
}