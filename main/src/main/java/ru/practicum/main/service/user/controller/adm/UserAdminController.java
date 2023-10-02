package ru.practicum.main.service.user.controller.adm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.user.dto.UserDto;
import ru.practicum.main.service.user.dto.UserShotDto;
import ru.practicum.main.service.user.service.api.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_FROM;
import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_SIZE;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> addUserAdmin(@RequestBody @Valid UserShotDto userShotDto) {
        log.info("UserController: receive POST request for add new user with body={}", userShotDto);
        UserDto savedUser = userService.addUserAdmin(userShotDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getAllUsersAdmin(
            @RequestParam(value = "ids", required = false, defaultValue = "") List<Long> ids,
            @RequestParam(name = "from", required = false, defaultValue = DEFAULT_FROM) @PositiveOrZero int from,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) @Positive int size
    ) {
        log.info("receive GET request for return users with ids={}, from={}, size={}", ids, from, size);
        Collection<UserDto> usersDto = userService.getAllUsersAdmin(ids, from, size);
        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUserByIdAdmin(
            @PathVariable("userId") @Positive Long userId
    ) {
        log.info("receive DELETE request fo delete user with userId= {}", userId);
        userService.deleteUserByIdAdmin(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
