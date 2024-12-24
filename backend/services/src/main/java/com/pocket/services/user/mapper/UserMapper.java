package com.pocket.services.user.mapper;

import org.mapstruct.Mapper;
import com.pocket.services.user.dto.request.RegisterUserDto;
import com.pocket.services.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUserModel(RegisterUserDto user);
    RegisterUserDto toUserDto(User user);
}
