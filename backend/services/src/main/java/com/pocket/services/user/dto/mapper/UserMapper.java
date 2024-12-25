package com.pocket.services.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pocket.services.user.dto.request.RegisterUserDto;
import com.pocket.services.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toUserModel(RegisterUserDto user);
}
