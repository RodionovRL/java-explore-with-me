package ru.practicum.main.service.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.user.dto.UserDto;
import ru.practicum.main.service.user.dto.UserShotDto;
import ru.practicum.main.service.user.service.api.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final static String DEFAULT_FROM = "0";
    private final static String DEFAULT_SIZE = "10";

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid UserShotDto userShotDto) {
        log.info("UserController: receive POST request for add new user with body={}", userShotDto);
        UserDto savedUser =  userService.addUser(userShotDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "id") long id) {
//        log.info("receive GET request for return user by id={}", id);
//        UserDto returnedUserDto = userService.getUserById(id);
//        return new ResponseEntity<>(returnedUserDto, HttpStatus.OK);
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,
//                                              @PathVariable(value = "id") long userId) {
//        log.info("receive PATCH request for update user with id={}, requestBody={}", userId, userDto);
//        UserDto updatedUserDto = userService.updateUser(userId, userDto);
//        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getAllUsers(
            @RequestParam(value = "ids", required = false, defaultValue = "") List<Long> ids,
            @RequestParam(name = "from", required = false, defaultValue = DEFAULT_FROM) int from,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size
    ) {
        log.info("receive GET request for return users with ids={}, from={}, size={}", ids, from, size);
        Collection<UserDto> usersDto = userService.getAllUsers(ids, from, size);
        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("userId") @NotEmpty Long userId) {
        log.info("receive DELETE request fo delete user with userId= {}", userId);
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
