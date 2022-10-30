package ru.practicum.stats.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    List<EndpointHit> findByUriInAndTimestampBetween(List<String> uri, LocalDateTime start, LocalDateTime end);
    List<EndpointHit> findDistinctByUriInAndTimestampBetween(List<String> uri, LocalDateTime start, LocalDateTime end);

    /*@Query(value =
            "SELECT app AS app, uri AS uri, COUNT(DISTINCT ip) AS views FROM hits " +
                    " WHERE uri IN :uris AND (timestamp >= :start AND timestamp < :end) GROUP BY app, uri"
    )
    List<ViewStats> findUniqueViews(@Param("uris") List<String> uris,
                                    @Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);

    @Query(value =
            "SELECT app AS app, uri AS uri, COUNT(ip) AS views FROM hits" +
                    " WHERE uri IN :uris AND (timestamp >= :start AND timestamp <= :end) GROUP BY app, uri"
    )
    List<ViewStats> findNotUniqueViews(@Param("uris") Collection<String> uris,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);*/

}
