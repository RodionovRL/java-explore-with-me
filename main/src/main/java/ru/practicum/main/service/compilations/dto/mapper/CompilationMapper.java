package ru.practicum.main.service.compilations.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main.service.compilations.dto.CompilationDto;
import ru.practicum.main.service.compilations.dto.NewCompilationDto;
import ru.practicum.main.service.compilations.dto.UpdateCompilationRequest;
import ru.practicum.main.service.compilations.model.Compilation;
import ru.practicum.main.service.events.dto.mapper.EventMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface CompilationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(UpdateCompilationRequest updateCompilationRequest);

    @Mapping(target = "events", source = "events", qualifiedByName = "toEventShortDtoList")
    CompilationDto toCompilationDto(Compilation compilation);

    List<CompilationDto> toDtoList(List<Compilation> compilations);

}
