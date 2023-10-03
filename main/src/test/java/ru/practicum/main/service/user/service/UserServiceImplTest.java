package ru.practicum.main.service.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.main.service.user.dto.UserDto;
import ru.practicum.main.service.user.dto.UserShotDto;
import ru.practicum.main.service.user.dto.mapper.UserMapper;
import ru.practicum.main.service.user.model.User;
import ru.practicum.main.service.user.repository.api.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_FROM;
import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_SIZE;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    final long userId = 1L;
    final String userName = "new User";
    final String email = "newUser@mail.com";
    int from = Integer.parseInt(DEFAULT_FROM);
    int size = Integer.parseInt(DEFAULT_SIZE);

    @Test
    void addUser_whenAddUser_thenUserAdded() {
        UserShotDto newUserShotDto = new UserShotDto(email, userName);
        UserDto newUserDto = new UserDto(email, userId, userName);
        User newUser = new User(userName, userId, email);
        when(userMapper.toUser(newUserShotDto)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(userMapper.toUserDto(newUser)).thenReturn(newUserDto);

        UserDto addedUserDto = userService.addUserAdmin(newUserShotDto);

        InOrder inOrder = inOrder(userMapper, userRepository, userMapper);

        assertAll(
                () -> inOrder.verify(userMapper).toUser(newUserShotDto),
                () -> inOrder.verify(userRepository).save(newUser),
                () -> inOrder.verify(userMapper).toUserDto(newUser),
                () -> assertEquals(addedUserDto, newUserDto),
                () -> assertEquals(addedUserDto.getEmail(), newUserShotDto.getEmail()),
                () -> assertEquals(addedUserDto.getName(), newUserShotDto.getName())
        );
    }

    @Test
    void getAllUsers_whenNeedToReturnUsersInIds_returnUsersInIds() {
        User user1 = new User("1" + userName, userId + 1, "1" + email);
        User user2 = new User("2" + userName, userId + 2, "2" + email);
        List<User> userList = List.of(user1, user2);
        UserDto userDto1 = new UserDto("1" + userName, userId + 1, "1" + email);
        UserDto userDto2 = new UserDto("2" + userName, userId + 2, "2" + email);
        List<UserDto> userDtoList = List.of(userDto1, userDto2);
        List<Long> ids = List.of(user1.getId(), user2.getId());
        when(userRepository.findAllByIdIn(eq(ids), any(Pageable.class))).thenReturn((userList));
        when(userMapper.toDtoList(userList)).thenReturn(userDtoList);

        List<UserDto> returnedUsersDto = userService.getAllUsersAdmin(ids, from, size);
        InOrder inOrder = inOrder(userRepository, userMapper);

        assertAll(
                () -> inOrder.verify(userRepository).findAllByIdIn(eq(ids), any(Pageable.class)),
                () -> inOrder.verify(userMapper).toDtoList(userList),
                () -> assertEquals(userDtoList, returnedUsersDto)
        );
    }
}