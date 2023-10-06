package ru.practicum.main.service.friendship.service;

import ru.practicum.main.service.friendship.dto.FriendshipDto;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface FriendshipService {
    FriendshipDto addNewFriendPrivate(Long userId, Long friendId);

    Collection<FriendshipDto> getAllUsersFriendsPrivate(Long userId, int from, int size);

    FriendshipDto getFriendByIdPrivate(long userId, Long friendId);

    void deleteFriendByIdPrivate(long userId, Long friendId);

    Collection<ParticipationRequestDto> getFriendsRequestsPrivate(Long userId, Long friendId, int from, int size);
}
