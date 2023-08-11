package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipantRequestDto;
import ru.practicum.ewm.request.model.ParticipantRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PrivateRequestController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipantRequestDto> get(@PathVariable("userId") long userId) {
        log.info("Получен запрос GET /users/{userId}/requests с параметрами userId = {}", userId);
        List<ParticipantRequest> event = requestService.getRequests(userId);
        return event.stream().map(ParticipantRequestMapper::toParticipantRequestDto).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ParticipantRequestDto add(@PathVariable("userId") long userId, @RequestParam("eventId") long eventId) {
        log.info("Получен запрос POST /users/{userId}/requests с параметрами userId = {}, eventId = {}", userId, eventId);
        ParticipantRequest request = requestService.add(userId, eventId);
        return ParticipantRequestMapper.toParticipantRequestDto(request);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipantRequestDto update(@PathVariable("userId") long userId, @PathVariable("requestId") long requestId) {
        log.info("Получен запрос PATCH /users/{userId}/requests/{requestId}/cancel с параметрами userId = {}, requestId = {}",
                userId, requestId);
        ParticipantRequest request = requestService.cancelRequest(userId, requestId);
        return ParticipantRequestMapper.toParticipantRequestDto(request);
    }
}
