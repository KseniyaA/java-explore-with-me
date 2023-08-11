package ru.practicum.ewm.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventFullDtoResponse;
import ru.practicum.ewm.event.dto.EventShortDtoResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDtoResponse> getAll(@RequestParam(value = "text", required = false) String text,
                                              @RequestParam(value = "categories", required = false) List<Long> categories,
                                              @RequestParam(value = "paid", required = false) Boolean paid,
                                              @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                              @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                              @RequestParam(value = "onlyAvailable", defaultValue = "FALSE") Boolean onlyAvailable,
                                              @RequestParam(value = "sort", defaultValue = "EVENT_DATE") String sort,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size,
                                              HttpServletRequest request) {
        log.info("Получен запрос GET /events с параметрами text = {}, categories = {}, paid = {}, rangeStart = {}, " +
                        "rangeEnd = {}, onlyAvailable = {}, sort = {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);
        List<Event> events = eventService.getAll(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request);
        return events.stream().map(EventMapper::toEventShortDtoResponse).collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventFullDtoResponse get(@PathVariable("eventId") long eventId, HttpServletRequest request) {
        log.info("Получен запрос GET /events/{eventId} с параметрами eventId = {}", eventId);
        Event event = eventService.getPublishedEvent(eventId, request);
        return EventMapper.toEventFullDtoResponse(event);
    }
}
