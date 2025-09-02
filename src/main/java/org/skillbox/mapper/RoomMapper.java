package org.skillbox.mapper;

import org.skillbox.dto.RoomRequest;
import org.skillbox.dto.RoomResponse;
import org.skillbox.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    Room toEntity(RoomRequest request);

    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(source = "hotel.name", target = "hotelName")
    RoomResponse toResponse(Room room);

    List<RoomResponse> toResponseList(List<Room> rooms);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    void updateEntityFromRequest(RoomRequest request, @MappingTarget Room room);
}