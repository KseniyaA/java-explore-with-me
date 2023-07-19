package ru.practicum.stats.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query(value = "select h.app, h.uri, count(h.id) as hits " +
            "from endpoint_hit as h " +
            "where h.created >= ?1 " +
            "and h.created <= ?2 " +
            "and h.uri in (?3) " +
            "group by h.app, h.uri " +
            "order by count(h.id) desc", nativeQuery = true)
    List<ViewStats> searchByParamsWithoutUnique(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query(value = "select h.app, h.uri, count(h.id) as hits " +
            "from endpoint_hit as h " +
            "where h.created >= ?1 " +
            "and h.created <= ?2 " +
            "group by h.app, h.uri " +
            "order by count(h.id) desc", nativeQuery = true)
    List<ViewStats> searchByParamsWithoutUnique(LocalDateTime start, LocalDateTime end);

    @Query(value = "select q.app, q.uri, count(1) as hits from (" +
            "select distinct h.app, h.uri, h.ip " +
            "from endpoint_hit as h " +
            "where h.created >= ?1 " +
            "and h.created <= ?2 " +
            "and h.uri in (?3) ) as q " +
            "group by q.app, q.uri " +
            "order by count(1) desc", nativeQuery = true)
    List<ViewStats> searchByParamsWithUnique(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query(value = "select q.app, q.uri, count(1) as hits from (" +
            "select distinct h.app, h.uri, h.ip " +
            "from endpoint_hit as h " +
            "where h.created >= ?1 " +
            "and h.created <= ?2 " +
            ") as q " +
            "group by q.app, q.uri " +
            "order by count(1) desc", nativeQuery = true)
    List<ViewStats> searchByParamsWithUnique(LocalDateTime start, LocalDateTime end);

}
