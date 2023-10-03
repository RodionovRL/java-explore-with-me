package ru.practicum.main.service.compilations.service;

import ru.practicum.main.service.compilations.dto.CompilationDto;
import ru.practicum.main.service.compilations.dto.NewCompilationDto;
import ru.practicum.main.service.compilations.dto.UpdateCompilationRequest;

import java.util.Collection;

public interface CompilationService {
    CompilationDto addCompilationAdmin(NewCompilationDto newCompilationDto);

    void deleteCompilationAdmin(Long compId);

    CompilationDto updateCompilationAdmin(Long compId, UpdateCompilationRequest updateCompilationRequest);

    Collection<CompilationDto> getAllCompilationsPub(boolean pinned, int from, int size);

    CompilationDto getCompilationByIdPub(Long compId);
}
