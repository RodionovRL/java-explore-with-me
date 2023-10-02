package ru.practicum.main.service.compilations.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.service.compilations.model.Compilation;

import java.util.List;
import java.util.Optional;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Optional<List<Compilation>> findAllByPinned(boolean pinned, Pageable pageable);

}
