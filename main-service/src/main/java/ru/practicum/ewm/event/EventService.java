package ru.practicum.ewm.event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    Event add(Long userId, Event toEvent);

    Event update(long userId, long eventId, Event toEvent);

    Event updateByAdmin(long eventId, Event toEvent);

    Event get(long userId, long eventId);

    List<Event> getAll(long userId, Integer from, Integer size);

    List<Event> getAll(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
                       Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request);

    List<Event> getAllByParams(List<Long> users, List<String> states, List<Long> categories, String rangeStart,
                               String rangeEnd, Integer from, Integer size);

    Event getPublishedEvent(long eventId, HttpServletRequest request);
}
