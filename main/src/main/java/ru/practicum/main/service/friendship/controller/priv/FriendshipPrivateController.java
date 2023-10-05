package ru.practicum.main.service.friendship.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.friendship.dto.FriendshipDto;
import ru.practicum.main.service.friendship.service.FriendshipService;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_FROM;
import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_SIZE;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/friends")
@RequiredArgsConstructor
public class FriendshipPrivateController {
    private final FriendshipService friendshipService;

    @PostMapping("/{friendId}")
    public ResponseEntity<FriendshipDto> addNewFriendPrivate(
            @PathVariable(name = "userId") @Positive Long userId,
            @PathVariable(name = "friendId") @Positive Long friendId
    ) {
        log.info("FriendshipPrivateController: receive POST request from userId={} for add new friend with id={}",
                userId, friendId);
        FriendshipDto newFriendshipDto = friendshipService.addNewFriendPrivate(userId, friendId);
        return new ResponseEntity<>(newFriendshipDto, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Collection<FriendshipDto>> getAllUsersFriendsPrivate(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "from", required = false, defaultValue = DEFAULT_FROM) int from,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size
    ) {
        log.info("FriendshipPrivateController: receive GET request for return All friends for userId={}, " +
                "from={}, size={}", userId, from, size);
        Collection<FriendshipDto> friendsDto =
                friendshipService.getAllUsersFriendsPrivate(userId, from, size);
        return new ResponseEntity<>(friendsDto, HttpStatus.OK);
    }

    @GetMapping("/{friendId}")
    public ResponseEntity<FriendshipDto> getUsersFriendByIdPrivate(
            @PathVariable(name = "userId") @Positive long userId,
            @PathVariable(name = "friendId") @Positive Long friendId
    ) {
        log.info("FriendshipPrivateController: receive GET request for return friend with friendId={}, for userId={}",
                friendId, userId);
        FriendshipDto friendDto = friendshipService.getFriendByIdPrivate(userId, friendId);
        return new ResponseEntity<>(friendDto, HttpStatus.OK);
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> deleteUsersFriendByIdPrivate(
            @PathVariable(name = "userId") @Positive long userId,
            @PathVariable(name = "friendId") @Positive Long friendId
    ) {
        log.info("FriendshipPrivateController: receive DELETE request for delete friend with friendId={}, " +
                "from userId={}", friendId, userId);
        friendshipService.deleteFriendByIdPrivate(userId, friendId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{friendId}/events")
    public ResponseEntity<Collection<ParticipationRequestDto>> getAllFriendsEventsPrivate(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "friendId") Long friendId,
            @RequestParam(name = "from", required = false, defaultValue = DEFAULT_FROM) @PositiveOrZero int from,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) @Positive int size
    ) {
        log.info("FriendshipPrivateController: receive GET request for return All friends events for userId={}, " +
                "friendId={}, from={}, size={}", userId, friendId, from, size);
        Collection<ParticipationRequestDto> friendsRequests =
                friendshipService.getFriendsRequestsPrivate(userId, friendId, from, size);
        return new ResponseEntity<>(friendsRequests, HttpStatus.OK);
    }
}
