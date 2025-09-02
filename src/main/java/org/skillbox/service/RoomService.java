package org.skillbox.service;

import org.skillbox.dto.PageResponse;
import org.skillbox.dto.RoomRequest;
import org.skillbox.dto.RoomResponse;
import org.skillbox.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.skillbox.mapper.RoomMapper;
import org.skillbox.model.Hotel;
import org.skillbox.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.skillbox.repository.jpa.HotelRepository;
import org.skillbox.repository.jpa.RoomRepository;
import org.skillbox.specification.RoomSpecification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;

    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комната не найдена с id: " + id));
        return roomMapper.toResponse(room);
    }

    public List<RoomResponse> getRoomsByHotelId(Long hotelId) {
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        return roomMapper.toResponseList(rooms);
    }

    public PageResponse<RoomResponse> getRoomsWithFilter(
            Long id, String name, BigDecimal minPrice, BigDecimal maxPrice,
            Integer maxGuests, LocalDate checkIn, LocalDate checkOut,
            Long hotelId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Specification<Room> spec = RoomSpecification.withFilters(
                id, name, minPrice, maxPrice, maxGuests, checkIn, checkOut, hotelId);

        Page<Room> roomPage = roomRepository.findAll(spec, pageable);

        PageResponse<RoomResponse> response = new PageResponse<>();
        response.setContent(roomMapper.toResponseList(roomPage.getContent()));
        response.setPage(roomPage.getNumber());
        response.setSize(roomPage.getSize());
        response.setTotalElements(roomPage.getTotalElements());
        response.setTotalPages(roomPage.getTotalPages());
        response.setLast(roomPage.isLast());

        return response;
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new NotFoundException("Отель не найден с id: " + request.getHotelId()));

        if (roomRepository.existsByNumber(request.getNumber())) {
            throw new IllegalArgumentException("Комната с таким номером " + request.getNumber() + " уже существует");
        }

        Room room = roomMapper.toEntity(request);
        room.setHotel(hotel);
        Room savedRoom = roomRepository.save(room);
        return roomMapper.toResponse(savedRoom);
    }

    @Transactional
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комната не найдена с id: " + id));

        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new NotFoundException("Отель не найден с id: " + request.getHotelId()));

        roomMapper.updateEntityFromRequest(request, room);
        room.setHotel(hotel);
        Room updatedRoom = roomRepository.save(room);
        return roomMapper.toResponse(updatedRoom);
    }

    @Transactional
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new NotFoundException("Комната не найдена с id: " + id);
        }
        roomRepository.deleteById(id);
    }

    public List<RoomResponse> getAvailableRooms(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
        List<Room> availableRooms = roomRepository.findAvailableRooms(hotelId, checkIn, checkOut);
        return roomMapper.toResponseList(availableRooms);
    }
}