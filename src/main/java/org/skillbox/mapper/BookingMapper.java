package org.skillbox.mapper;

import org.skillbox.dto.BookingRequest;
import org.skillbox.dto.BookingResponse;
import org.skillbox.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "user", ignore = true)
    Booking toEntity(BookingRequest request);

    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "room.number", target = "roomNumber")
    @Mapping(source = "room.hotel.id", target = "hotelId")
    @Mapping(source = "room.hotel.name", target = "hotelName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    BookingResponse toResponse(Booking booking);

    List<BookingResponse> toResponseList(List<Booking> bookings);
}