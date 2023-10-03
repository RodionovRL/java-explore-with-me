package ru.practicum.main.service.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.compilations.dto.CompilationDto;
import ru.practicum.main.service.compilations.dto.NewCompilationDto;
import ru.practicum.main.service.compilations.dto.UpdateCompilationRequest;
import ru.practicum.main.service.compilations.dto.mapper.CompilationMapper;
import ru.practicum.main.service.compilations.model.Compilation;
import ru.practicum.main.service.compilations.repository.CompilationRepository;
import ru.practicum.main.service.events.model.Event;
import ru.practicum.main.service.events.repository.EventRepository;
import ru.practicum.main.service.exception.NotFoundException;
import ru.practicum.main.service.utils.PageRequestUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public CompilationDto addCompilationAdmin(NewCompilationDto newCompilationDto) {
        Compilation newCompilation = compilationMapper.toCompilation(newCompilationDto);
        if (newCompilationDto.getEvents() != null) {
            List<Event> events = findEventsByIds(newCompilationDto.getEvents());
            newCompilation.setEvents(events);
        }
        Compilation savedCompilation = compilationRepository.save(newCompilation);
        log.info("CompilationService: Admin was save compilation={}", savedCompilation);
        return compilationMapper.toCompilationDto(savedCompilation);
    }

    @Override
    public void deleteCompilationAdmin(Long compId) {
        Compilation compilation = findCompilationById(compId);
        compilationRepository.deleteById(compId);
        log.info("CompilationService: Admin was delete compilation={}", compilation);
    }

    @Override
    public CompilationDto updateCompilationAdmin(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation oldCompilation = findCompilationById(compId);
        Compilation newCompilation = compilationMapper.toCompilation(updateCompilationRequest);
        newCompilation.setId(compId);
        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = findEventsByIds(updateCompilationRequest.getEvents());
            newCompilation.setEvents(events);
        } else {
            newCompilation.setEvents(oldCompilation.getEvents());
        }
        if (updateCompilationRequest.getPinned() == null) {
            newCompilation.setPinned(oldCompilation.getPinned());
        }
        if (updateCompilationRequest.getTitle() == null) {
            newCompilation.setTitle(oldCompilation.getTitle());
        }
        Compilation updatedCompilation = compilationRepository.save(newCompilation);
        log.info("CompilationService: Admin was update compilation. " +
                        "oldCompilation={}, newCompilation={}, updatedCompilation={}",
                oldCompilation, newCompilation, updatedCompilation);
        return compilationMapper.toCompilationDto(updatedCompilation);
    }

    @Override
    public Collection<CompilationDto> getAllCompilationsPub(boolean pinned, int from, int size) {
        List<Compilation> allCompilations =
                compilationRepository.findAllByPinned(pinned, PageRequestUtil.of(from, size))
                        .orElse(Collections.emptyList());
        log.info("compilationService: returned all {} compilations", allCompilations.size());
        return compilationMapper.toDtoList(allCompilations);
    }

    @Override
    public CompilationDto getCompilationByIdPub(Long compId) {
        Compilation compilation = findCompilationById(compId);
        log.info("compilationService: returned compilation with id={}", compilation);
        return compilationMapper.toCompilationDto(compilation);
    }

    private Compilation findCompilationById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Compilation with id=%d not found", compId)));
    }

    private List<Event> findEventsByIds(Iterable<Long> eventsIds) {
        return eventRepository.findAllByIdIn(eventsIds).orElseThrow(() ->
                new NotFoundException(String.format("Events with ids=%s", eventsIds))
        );
    }

}
