package ru.practicum.main.service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.exception.NotFoundException;
import ru.practicum.main.service.user.dto.UserDto;
import ru.practicum.main.service.user.dto.UserShotDto;
import ru.practicum.main.service.user.dto.mapper.UserMapper;
import ru.practicum.main.service.user.model.User;
import ru.practicum.main.service.user.repository.api.UserRepository;
import ru.practicum.main.service.user.service.api.UserService;
import ru.practicum.main.service.utils.PageRequestUtil;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional()
    @Override
    public UserDto addUser(UserShotDto newUserDto) {
        User newUser = userMapper.toUser(newUserDto);
        User addedUser = userRepository.save(newUser);
        log.info("userService: was add user={}", addedUser);
        return userMapper.toUserDto(addedUser);
    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public UserDto getUserById(Long userId) {
//        User user = findUserById(userId);
//        log.info("userService: was returned user={}, by id={}", user, userId);
//        return userMapper.toUserDto(user);
//    }
//
//    @Transactional
//    @Override
//    public UserDto updateUser(long userId, UserDto userDto) {
//        User oldUser = findUserById(userId);
//        User newUser = userMapper.toUser(userDto);
//        newUser.setId(userId);
//
//        if (Objects.isNull(newUser.getName())) {
//            newUser.setName(oldUser.getName());
//        }
//
//        if (Objects.isNull(newUser.getEmail())) {
//            newUser.setEmail(oldUser.getEmail());
//        }
//
//        User updatedUser = userRepository.save(newUser);
//        log.info("userService: old user={} update to new user={}", oldUser, updatedUser);
//
//        return userMapper.toUserDto(updatedUser);
//    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequestUtil.of(from, size);
        List<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(pageable).getContent();
        } else {
            users = userRepository.findAllByIdIn(ids, pageable);
        }
        log.info("userService: returned all {} users", users.size());
        return userMapper.map(users);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        findUserById(id);
        userRepository.deleteById(id);
        log.info("userService: delete user with id={}", id);
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d was not found", userId)));
    }
}
