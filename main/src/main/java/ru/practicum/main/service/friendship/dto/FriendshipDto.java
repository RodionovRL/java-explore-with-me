package ru.practicum.main.service.friendship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.service.user.dto.UserDto;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipDto {
    private UserDto user;
    private UserDto friend;
}
