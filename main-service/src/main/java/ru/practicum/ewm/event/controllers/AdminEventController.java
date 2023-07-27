package ru.practicum.ewm.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventFullDtoResponse;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/admin/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<EventFullDtoResponse> getAll(@RequestParam(value = "users", required = false) List<Long> users,
                                              @RequestParam(value = "states", required = false) List<String> states,
                                              @RequestParam(value = "categories", required = false) List<Long> categories,
                                              @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                              @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /admin/events с параметрами users = {}, states = {}, categories = {}, " +
                        "rangeStart = {}, rangeEnd = {}, from = {}, size = {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        List<Event> events = eventService.getAllByParams(users, states, categories, rangeStart, rangeEnd, from, size);
        return events.stream().map(EventMapper::toEventFullDtoResponse).collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(code = HttpStatus.OK)
    public EventFullDtoResponse update(@PathVariable("eventId") long eventId,
                                       @RequestBody @Valid UpdateEventAdminRequest dto) {
        log.info("Получен запрос PATCH /admin/events/{eventId} с параметрами eventId = {}, " +
                "dto = {}", eventId, dto);
        Event event = eventService.updateByAdmin(eventId, EventMapper.toEvent(dto));
        return EventMapper.toEventFullDtoResponse(event);
    }
}