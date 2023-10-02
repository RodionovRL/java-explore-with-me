package ru.practicum.main.service.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.service.utils.EventState;
import ru.practicum.main.service.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator_Id(long userId, Pageable pageable);

    Optional<Event> findByInitiator_IdAndId(long userId, long eventId);

    @Query(value = "select e " +
            "from Event e " +
            "where (:userIds is null or e.initiator.id in :userIds) and " +
            "(:states is null or e.state in :states) and " +
            "(:categoryIds is null or e.category.id in :categoryIds) and " +
            "e.eventDate > :start and (cast(:end as date) is null or e.eventDate < :end)")
    Optional<List<Event>> findEventForAdmin(
            @Param("userIds") List<Long> userIds,
            @Param("states") List<EventState> states,
            @Param("categoryIds") List<Long> categoryIds,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );

    @Query(value = "select e " +
            "from Event e " +
            "where e.state = :state and " +
            "(:text is null or " +
            "e.description like concat('%', :text, '%') or e.annotation like concat('%', :text, '%')) and " +
            "(:categories is null or e.category.id in :categories) and " +
            "e.eventDate > :start and " +
            "(cast(:end as date) is null or e.eventDate < :end) and " +
            "(:onlyAvailable is false or " +
            "(:onlyAvailable is true and e.confirmedRequests < e.participantLimit or e.participantLimit = 0))")
    Optional<List<Event>> findEventPublic(
            @Param("state") EventState state,
            @Param("text") String text,
            @Param("categories") List<Long> categoryIds,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("onlyAvailable") Boolean onlyAvailable,
            Pageable pageable
    );

    Optional<List<Event>> findAllByIdIn(Iterable<Long> ids);
}
