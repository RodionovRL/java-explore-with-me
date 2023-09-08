package ru.practicum.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query(value = "select new ru.practicum.model.ViewStats(eh.app, eh.uri, count (distinct eh.ip)) " +
            "from EndpointHit eh " +
            "where eh.uri in :uris and eh.timestamp > :start and eh.timestamp < :end " +
            "group by eh.uri, eh.app " +
            "order by count (eh.uri) desc")
    List<ViewStats> findStatsByUrisUniqueIp(
            @Param("uris") List<String> uris,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = "select new ru.practicum.model.ViewStats(eh.app, eh.uri, count (eh.ip)) " +
            "from EndpointHit eh " +
            "where eh.uri in :uris and eh.timestamp > :start and eh.timestamp < :end " +
            "group by eh.uri, eh.app " +
            "order by count (eh.uri) desc")
    List<ViewStats> findStatsByUrisNotUniqueIp(
            @Param("uris") List<String> uris,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = "select new ru.practicum.model.ViewStats(eh.app, eh.uri, count (distinct eh.ip)) " +
            "from EndpointHit eh " +
            "where eh.timestamp > :start and eh.timestamp < :end " +
            "group by eh.uri, eh.app " +
            "order by count (eh.uri) desc")
    List<ViewStats> findStatsAllUrisUniqueIp(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = "select new ru.practicum.model.ViewStats(eh.app, eh.uri, count (eh.ip)) " +
            "from EndpointHit eh " +
            "where eh.timestamp > :start and eh.timestamp < :end " +
            "group by eh.uri, eh.app " +
            "order by count (eh.uri) desc")
    List<ViewStats> findStatsAllUrisNotUniqueIp(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}

