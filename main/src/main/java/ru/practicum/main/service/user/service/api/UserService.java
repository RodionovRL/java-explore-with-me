package ru.practicum.main.service.user.service.api;

import ru.practicum.main.service.user.dto.UserDto;
import ru.practicum.main.service.user.dto.UserShotDto;

import java.util.Collection;
import java.util.List;

public interface UserService {
    UserDto addUser(UserShotDto newUserDto);
//
//    UserDto getUserById(Long id);
//
//    UserDto updateUser(long userId, UserDto userDto);
//
    Collection<UserDto> getAllUsers(List<Long> ids, int from, int size);

    void deleteUserById(Long id);
}
