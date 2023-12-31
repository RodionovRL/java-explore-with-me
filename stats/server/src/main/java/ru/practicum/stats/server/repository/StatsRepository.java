package ru.practicum.stats.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stats.server.model.EndpointHit;
import ru.practicum.stats.server.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query(value = "select new ru.practicum.stats.server.model.ViewStats(eh.app, eh.uri, count (distinct eh.ip)) " +
            "from EndpointHit eh " +
            "where eh.uri in :uris and eh.timestamp between :start and :end " +
            "group by eh.uri, eh.app " +
            "order by count (eh.ip) desc")
    List<ViewStats> findStatsByUrisUniqueIp(
            @Param("uris") List<String> uris,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = "select new ru.practicum.stats.server.model.ViewStats(eh.app, eh.uri, count (eh.ip)) " +
            "from EndpointHit eh " +
            "where eh.uri in :uris and eh.timestamp between :start and :end " +
            "group by eh.uri, eh.app " +
            "order by count (eh.ip) desc")
    List<ViewStats> findStatsByUrisNotUniqueIp(
            @Param("uris") List<String> uris,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = "select new ru.practicum.stats.server.model.ViewStats(eh.app, eh.uri, count (distinct eh.ip)) " +
            "from EndpointHit eh " +
            "where eh.timestamp between :start and :end " +
            "group by eh.uri, eh.app " +
            "order by count (eh.ip) desc")
    List<ViewStats> findStatsAllUrisUniqueIp(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = "select new ru.practicum.stats.server.model.ViewStats(eh.app, eh.uri, count (eh.ip)) " +
            "from EndpointHit eh " +
            "where eh.timestamp between :start and :end " +
            "group by eh.uri, eh.app " +
            "order by count (eh.ip) desc")
    List<ViewStats> findStatsAllUrisNotUniqueIp(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}

