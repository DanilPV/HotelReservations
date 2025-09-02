package org.skillbox.mapper;

import org.skillbox.dto.UserRequest;
import org.skillbox.dto.UserResponse;
import org.skillbox.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(UserRequest request, @MappingTarget User user);
}