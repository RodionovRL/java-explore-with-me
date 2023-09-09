package ru.practicum.main.service.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main.service.user.dto.UserDto;
import ru.practicum.main.service.user.dto.UserShotDto;
import ru.practicum.main.service.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    User toUser(UserShotDto userShotDto);

    List<UserDto> map(List<User> users);
}
