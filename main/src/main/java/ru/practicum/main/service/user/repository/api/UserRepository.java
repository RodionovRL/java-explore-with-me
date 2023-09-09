package ru.practicum.main.service.user.repository.api;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.service.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    public List<User> findAllByIdIn(List<Long> ids, Pageable pageable);
}
