package ru.practicum.main.service.user.service.api;

import ru.practicum.main.service.user.dto.UserDto;
import ru.practicum.main.service.user.dto.UserShotDto;

import java.util.Collection;
import java.util.List;

public interface UserService {
    UserDto addUserAdmin(UserShotDto newUserDto);

    Collection<UserDto> getAllUsersAdmin(List<Long> ids, int from, int size);

    void deleteUserByIdAdmin(Long id);
}
