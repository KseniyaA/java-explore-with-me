package ru.practicum.ewm.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventDtoRequest;
import ru.practicum.ewm.event.dto.EventFullDtoResponse;
import ru.practicum.ewm.event.dto.EventShortDtoResponse;
import ru.practicum.ewm.request.ParticipantRequestMapper;
import ru.practicum.ewm.request.RequestService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.ewm.request.dto.ParticipantRequestDto;
import ru.practicum.ewm.request.model.ParticipantRequest;
import ru.practicum.ewm.valid.Marker;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PrivateEventController {
    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping
    public List<EventShortDtoResponse> getAll(@PathVariable("userId") long userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /users/{userId}/events с параметрами userId = {}, from = {}, size = {}",
                userId, from, size);
        List<Event> events = eventService.getAll(userId, from, size);
        Map<Long, Integer> allConfirmedRequests = requestService.getAllConfirmedRequests();
        return events.stream().map(x -> EventMapper.toEventShortDtoResponse(x, allConfirmedRequests.get(x.getId())))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EventFullDtoResponse add(@PathVariable("userId") long userId,
                                    @RequestBody @Validated(Marker.OnCreate.class) EventDtoRequest dto) {
        log.info("Получен запрос POST /users/{userId}/events с параметрами userId = {}, dto = {}", userId, dto);
        Event event = eventService.add(userId, EventMapper.toEvent(dto));
        Map<Long, Integer> allConfirmedRequests = requestService.getAllConfirmedRequests();
        log.info("Создан event с id = {}", event.getId());
        return EventMapper.toEventFullDtoResponse(event, allConfirmedRequests.get(event.getId()));
    }

    @GetMapping("/{eventId}")
    public EventFullDtoResponse get(@PathVariable("userId") long userId,
                                    @PathVariable("eventId") long eventId) {
        log.info("Получен запрос GET /users/{userId}/events/{eventId} с параметрами userId = {}, eventId = {}", userId, eventId);
        Event event = eventService.get(userId, eventId);
        Map<Long, Integer> allConfirmedRequests = requestService.getAllConfirmedRequests();
        return EventMapper.toEventFullDtoResponse(event, allConfirmedRequests.get(event.getId()));
    }

    @PatchMapping("/{eventId}")
    public EventFullDtoResponse update(@PathVariable("userId") long userId,
                                       @PathVariable("eventId") long eventId,
                                       @RequestBody @Validated(Marker.OnUpdate.class) EventDtoRequest dto) {
        log.info("Получен запрос PATCH /users/{userId}/events/{eventId} с параметрами userId = {}, eventId = {}, " +
                "dto = {}", userId, eventId, dto);
        Event event = eventService.update(userId, eventId, EventMapper.toEvent(dto));
        Map<Long, Integer> allConfirmedRequests = requestService.getAllConfirmedRequests();
        return EventMapper.toEventFullDtoResponse(event, allConfirmedRequests.get(event.getId()));
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipantRequestDto> getRequests(@PathVariable("userId") long userId,
                                                   @PathVariable("eventId") long eventId) {
        log.info("Получен запрос GET /users/{userId}/events/{eventId}/requests с параметрами userId = {}, eventId = {}",
                userId, eventId);
        List<ParticipantRequest> requests = requestService.getRequests(userId, eventId);
        return requests.stream().map(ParticipantRequestMapper::toParticipantRequestDto).collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResultDto changeStatus(@PathVariable("userId") long userId,
                                                          @PathVariable("eventId") long eventId,
                                                          @RequestBody(required = true) EventRequestStatusUpdateRequestDto requestsDto) {
        log.info("Получен запрос PATCH /users/{userId}/events/{eventId}/requests с параметрами userId = {}, " +
                        "eventId = {}, dto = {}", userId, eventId, requestsDto);
        return ParticipantRequestMapper.toEventRequestStatusUpdateRequestDto(
                requestService.changeRequestsStatus(userId, eventId,
                        requestsDto == null ? null : requestsDto.getRequests(),
                        requestsDto  == null ? null : requestsDto.getStatus()));
    }
}
