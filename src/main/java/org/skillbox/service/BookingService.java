package org.skillbox.service;

import org.skillbox.dto.BookingRequest;
import org.skillbox.dto.BookingResponse;
import org.skillbox.dto.PageResponse;
import org.skillbox.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.skillbox.mapper.BookingMapper;
import org.skillbox.model.Booking;
import org.skillbox.model.Room;
import org.skillbox.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.skillbox.repository.jpa.BookingRepository;
import org.skillbox.repository.jpa.RoomRepository;
import org.skillbox.repository.jpa.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final KafkaProducerService kafkaProducerService;

    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено id: " + id));
        return bookingMapper.toResponse(booking);
    }

    public List<BookingResponse> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return bookingMapper.toResponseList(bookings);
    }

    public PageResponse<BookingResponse> getAllBookings(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> bookingPage = bookingRepository.findAll(pageable);

        PageResponse<BookingResponse> response = new PageResponse<>();
        response.setContent(bookingMapper.toResponseList(bookingPage.getContent()));
        response.setPage(bookingPage.getNumber());
        response.setSize(bookingPage.getSize());
        response.setTotalElements(bookingPage.getTotalElements());
        response.setTotalPages(bookingPage.getTotalPages());
        response.setLast(bookingPage.isLast());

        return response;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new NotFoundException("Комната не найдена id: " + request.getRoomId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден id: " + request.getUserId()));

        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(
                request.getRoomId(), request.getCheckInDate(), request.getCheckOutDate());

        if (!conflictingBookings.isEmpty()) {
            throw new IllegalArgumentException("Комната не доступна для выбранных дат");
        }

        if (request.getCheckInDate().isBefore(LocalDate.now()) ||
                request.getCheckOutDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Бронирование не может быть создано в прошлом");
        }

        if (request.getCheckOutDate().isBefore(request.getCheckInDate()) ||
                request.getCheckOutDate().isEqual(request.getCheckInDate())) {
            throw new IllegalArgumentException("Проверьте даты бронирования");
        }

        Booking booking = bookingMapper.toEntity(request);
        booking.setRoom(room);
        booking.setUser(user);

        Booking savedBooking = bookingRepository.save(booking);

        kafkaProducerService.sendBookingEvent(
                savedBooking.getId(),
                user.getId(),
                request.getCheckInDate(),
                request.getCheckOutDate());

        return bookingMapper.toResponse(savedBooking);
    }

    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено id: " + id));

        if (booking.getCheckInDate().minusDays(1).isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Бронирование не может быть отменено в течении 24 часов от даты бронирования");
        }

        bookingRepository.deleteById(id);
    }
}