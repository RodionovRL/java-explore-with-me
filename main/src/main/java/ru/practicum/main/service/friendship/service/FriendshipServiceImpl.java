package ru.practicum.main.service.friendship.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.exception.NotFoundException;
import ru.practicum.main.service.friendship.dto.FriendshipDto;
import ru.practicum.main.service.friendship.dto.mapper.FriendshipMapper;
import ru.practicum.main.service.friendship.model.Friendship;
import ru.practicum.main.service.friendship.repository.FriendshipRepository;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.main.service.request.dto.mapper.RequestMapper;
import ru.practicum.main.service.request.model.ParticipationRequest;
import ru.practicum.main.service.request.repository.api.RequestRepository;
import ru.practicum.main.service.user.model.User;
import ru.practicum.main.service.user.repository.api.UserRepository;
import ru.practicum.main.service.utils.PageRequestUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final FriendshipMapper friendshipMapper;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Transactional
    @Override
    public FriendshipDto addNewFriendPrivate(Long userId, Long friendId) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);
        Friendship newFriendship = Friendship.builder()
                .user(user)
                .friend(friend)
                .build();

        Friendship addedFriendship = friendshipRepository.save(newFriendship);
        log.info("friendshipService: was add friendship={}", addedFriendship);
        return friendshipMapper.toFriendshipDto(addedFriendship);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<FriendshipDto> getAllUsersFriendsPrivate(Long userId, int from, int size) {
        findUserById(userId);
        List<Friendship> allFriendships = friendshipRepository.findAllByUserId(userId, PageRequestUtil.of(from, size))
                .orElse(Collections.emptyList());
        log.info("friendshipService: returned all {} friendships", allFriendships.size());

        return friendshipMapper.toDtoList(allFriendships);
    }

    @Transactional(readOnly = true)
    @Override
    public FriendshipDto getFriendByIdPrivate(long userId, Long friendId) {
        findUserById(userId);
        findUserById(friendId);
        Friendship usersFriendship = findUsersFriend(userId, friendId);
        log.info("friendshipService: returned friendship={}", usersFriendship);
        return friendshipMapper.toFriendshipDto(usersFriendship);
    }

    @Transactional
    @Override
    public void deleteFriendByIdPrivate(long userId, Long friendId) {
        findUserById(userId);
        findUserById(friendId);
        Friendship usersFriendship = findUsersFriend(userId, friendId);
        friendshipRepository.delete(usersFriendship);
        log.info("userService: deleted friend with id={} from user with id={}", friendId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getFriendsRequestsPrivate(Long userId, Long friendId, int from, int size) {
        findUserById(userId);
        findUserById(friendId);
        checkMutualFriendship(userId, friendId);
        List<ParticipationRequest> friendsRequests = requestRepository.findAllByRequesterIdAndSubscriptionPermit(
                friendId, true, PageRequestUtil.of(from, size)).orElse(Collections.emptyList());
        log.info("friendshipService: returned all {} requests", friendsRequests.size());
        return requestMapper.toDtoList(friendsRequests);
    }

    private void checkMutualFriendship(Long userId, Long friendId) {
        findUsersFriend(userId, friendId);
        findUsersFriend(friendId, userId);
    }

    private Friendship findUsersFriend(Long userId, Long friendId) {
        return friendshipRepository.findByUserIdAndFriendId(userId, friendId).orElseThrow(() ->
                new NotFoundException(String.format("Friendship between userId=%d and friendId=%d was not found",
                        userId, friendId)));
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

}
