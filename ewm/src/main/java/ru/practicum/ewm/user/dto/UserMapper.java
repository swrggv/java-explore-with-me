package ru.practicum.ewm.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
    UserDto toUserDto(User user);
    List<UserDto> toUserDtoList(List<User> users);
    @Mapping(target = "id", ignore = true)
    User toUser(NewUserRequest userRequest);
    UserShortDto toUserShortDto(User user);
}
