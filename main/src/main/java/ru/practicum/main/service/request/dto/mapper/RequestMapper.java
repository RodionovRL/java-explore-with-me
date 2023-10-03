package ru.practicum.main.service.request.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.main.service.request.model.ParticipationRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "event",source = "eventId")
    @Mapping(target = "requester", source = "requesterId")
    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest);

    @Mapping(target = "eventId",source = "event")
    @Mapping(target = "requesterId", source = "requester")
    ParticipationRequest toParticipationRequest(ParticipationRequestDto participationRequestDto);

    List<ParticipationRequestDto> toDtoList(List<ParticipationRequest> participationRequests);
}
