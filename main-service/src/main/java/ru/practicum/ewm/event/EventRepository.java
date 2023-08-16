package ru.practicum.ewm.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.user.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Page<Event> findByInitiator(User initiator, Pageable page);

    List<Event> findByCategory(Category category);

    boolean existsByCategory(Category category);

    @Query(value = "select e.* " +
            "from event as e " +
            "left join location as l on e.location_id = l.id " +
            "where distance(l.lat, l.lon, ?1, ?2) <= ?3 " +
            "and e.status = 'PUBLISHED'", nativeQuery = true)
    List<Event> getEventsByLocation(Double lat, Double lon, Double radius);
}
