package ru.practicum.main.service.friendship.dto.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.service.friendship.dto.FriendshipDto;
import ru.practicum.main.service.friendship.model.Friendship;
import ru.practicum.main.service.user.dto.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring",  uses = UserMapper.class)
public interface FriendshipMapper {
    FriendshipDto toFriendshipDto(Friendship friendship);

    List<FriendshipDto> toDtoList(List<Friendship> friends);
}
