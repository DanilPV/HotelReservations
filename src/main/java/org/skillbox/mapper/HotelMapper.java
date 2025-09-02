package org.skillbox.mapper;

import org.skillbox.dto.HotelRequest;
import org.skillbox.dto.HotelResponse;
import org.skillbox.model.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    Hotel toEntity(HotelRequest request);

    HotelResponse toResponse(Hotel hotel);

    List<HotelResponse> toResponseList(List<Hotel> hotels);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    void updateEntityFromRequest(HotelRequest request, @MappingTarget Hotel hotel);
}