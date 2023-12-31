package ru.practicum.ewm.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventFullDtoResponse;
import ru.practicum.ewm.event.dto.EventShortDtoResponse;
import ru.practicum.ewm.request.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/events")
@Slf4j
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService eventService;
    private final RequestService requestService;

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
        Map<Long, Integer> allConfirmedRequests = requestService.getAllConfirmedRequests();
        return events.stream()
                .map(x -> EventMapper.toEventShortDtoResponse(x, allConfirmedRequests.getOrDefault(x.getId(), 0)))
                .collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventFullDtoResponse get(@PathVariable("eventId") long eventId, HttpServletRequest request) {
        log.info("Получен запрос GET /events/{eventId} с параметрами eventId = {}", eventId);
        Event event = eventService.getPublishedEvent(eventId, request);
        Map<Long, Integer> allConfirmedRequests = requestService.getAllConfirmedRequests();
        return EventMapper.toEventFullDtoResponse(event, allConfirmedRequests.getOrDefault(eventId, 0));
    }

    @GetMapping("/location")
    public List<EventShortDtoResponse> getAllByLocation(@RequestParam(value = "lat") Double lat,
                                                        @RequestParam(value = "lon") Double lon,
                                                        @RequestParam(value = "radius") Double radius) {
        log.info("Получен запрос GET /events/location с параметрами lat = {}, lon = {}, radius = {}", lat, lon, radius);
        List<Event> eventsByLocation = eventService.getEventsByLocation(lat, lon, radius);
        Map<Long, Integer> allConfirmedRequests = requestService.getAllConfirmedRequests();
        return eventsByLocation.stream()
                .map(x -> EventMapper.toEventShortDtoResponse(x, allConfirmedRequests.getOrDefault(x.getId(), 0)))
                .collect(Collectors.toList());
    }
}
