package ru.practicum.main.service.compilations.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.compilations.dto.CompilationDto;
import ru.practicum.main.service.compilations.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_FROM;
import static ru.practicum.main.service.utils.PageRequestUtil.DEFAULT_SIZE;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping("")
    public ResponseEntity<Collection<CompilationDto>> getAllCompilationsPub(
            @RequestParam(name = "pinned", required = false, defaultValue = "false") boolean pinned,
            @RequestParam(name = "from", required = false, defaultValue = DEFAULT_FROM) @PositiveOrZero int from,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) @Positive int size
    ) {
        log.info("receive GET request for return compilation from={}, size={}, pinned={}", from, size, pinned);
        Collection<CompilationDto> compilationsDto = compilationService.getAllCompilationsPub(pinned, from, size);
        return new ResponseEntity<>(compilationsDto, HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilationByIdPub(
            @PathVariable("compId") @Positive Long compId
    ) {
        log.info("receive GET request for return compilation by compilationId={}", compId);
        CompilationDto compilationDto = compilationService.getCompilationByIdPub(compId);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }
}
