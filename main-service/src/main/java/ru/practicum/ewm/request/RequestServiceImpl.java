package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.EventStatus;
import ru.practicum.ewm.exception.ConflictOperationException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.model.ParticipantRequest;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    @Transactional
    @Override
    public ParticipantRequest add(long userId, long eventId) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);

        checkRequest(user, event);

        RequestStatus requestStatus;
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            requestStatus = RequestStatus.CONFIRMED;
        } else {
            requestStatus = RequestStatus.PENDING;
        }

        ParticipantRequest request = ParticipantRequest.builder()
                .requester(user)
                .created(LocalDateTime.now())
                .status(requestStatus)
                .event(event).build();
        ParticipantRequest createdRequest = requestRepository.save(request);
        return createdRequest;
    }

    @Override
    public List<ParticipantRequest> getRequests(long userId) {
        User user = getUserById(userId);
        return requestRepository.findAllByRequester(user);
    }

    @Override
    public List<ParticipantRequest> getRequests(long userId, long eventId) {
        Event event = getEventById(eventId);
        return requestRepository.findAllByEvent(event);

    }

    @Transactional
    @Override
    public ParticipantRequest cancelRequest(long userId, long requestId) {
        getUserById(userId);
        ParticipantRequest request = getRequestById(requestId);
        request.setStatus(RequestStatus.CANCELED);
        return requestRepository.save(request);
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult changeRequestsStatus(long userId, long eventId,
                                                               List<Long> requestsIds, RequestStatus status) {
        getUserById(userId);
        Event event = getEventById(eventId);
        List<ParticipantRequest> confirmedRequests = getConfirmedRequests(event);
        if (event.getParticipantLimit() != 0 && confirmedRequests.size() >= event.getParticipantLimit()) {
            throw new ConflictOperationException("Достигнут лимит по заявкам на данное событие");
        }

        // Если не трубется модерация по событию
        if (event.getParticipantLimit().equals(0) || event.getRequestModeration().equals(Boolean.FALSE)) {
            List<ParticipantRequest>  requestsByIds = requestRepository.findAllByEvent(event);

            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(requestsByIds).build();
        }

        List<ParticipantRequest> requestForConfirm = (requestsIds == null)
                ? requestRepository.findAllByEvent(event)
                : requestRepository.findAllById(requestsIds);

        confirmOrRejectRequests(event, requestForConfirm,
                status == null ? RequestStatus.CONFIRMED : status);

        List<ParticipantRequest> confirmedOrRejectRequests = requestRepository.findAllById(requestForConfirm
                .stream().map(ParticipantRequest::getId).collect(Collectors.toList()));

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedOrRejectRequests
                                .stream().filter(x -> x.getStatus().equals(RequestStatus.CONFIRMED))
                                .collect(Collectors.toList()))
                .rejectedRequests(confirmedOrRejectRequests
                                .stream().filter(x -> x.getStatus().equals(RequestStatus.REJECTED))
                                .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<ParticipantRequest> getConfirmedRequests(Event event) {
        return requestRepository.findAllByEventAndStatus(event, RequestStatus.CONFIRMED);
    }

    @Override
    public Map<Long, Integer> getAllConfirmedRequests() {
        List<RequestsByStatus> eventRequestsByStatus = requestRepository.getEventRequestsByStatus(RequestStatus.CONFIRMED.name());
        return eventRequestsByStatus.stream()
                .collect(Collectors.toMap(RequestsByStatus::getEventId, RequestsByStatus::getRequestsSize));
    }

    @Transactional
    private void confirmOrRejectRequests(Event event, List<ParticipantRequest> requests, RequestStatus status) {
        int confirmedRequests = getConfirmedRequests(event).size();
        if (requests != null) {
            for (ParticipantRequest request : requests) {
                if (status.equals(RequestStatus.CONFIRMED)) {
                    if (confirmedRequests < event.getParticipantLimit()) {
                        if (request.getStatus().equals(RequestStatus.PENDING)) {
                            request.setStatus(RequestStatus.CONFIRMED);
                            requestRepository.save(request);
                        }
                    } else {
                        request.setStatus(RequestStatus.REJECTED);
                        requestRepository.save(request);
                    }
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                    requestRepository.save(request);
                }
            }
        }
    }

    @Transactional
    private void checkRequest(User requester, Event event) {
        ParticipantRequest allByRequesterAndEvent = requestRepository.findAllByRequesterAndEvent(requester, event);
        if (allByRequesterAndEvent != null) {
            throw new ConflictOperationException("Пользователь уже зарегестрирован на данное событие");
        }
        if (event.getInitiator().getId().equals(requester.getId())) {
            throw new ConflictOperationException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!EventStatus.PUBLISHED.equals(event.getEventStatus())) {
            throw new ConflictOperationException("Нельзя участвовать в неопубликованном событии");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(getConfirmedRequests(event).size())) {
            throw new ConflictOperationException("Достигнут лимит запросов");
        }
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new EntityNotFoundException("Пользователь с id = " + userId + " не существует");
        });
    }

    public Event getEventById(long id) {
        return eventRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Событие с id = " + id + " не существует");
        });
    }

    private ParticipantRequest getRequestById(long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> {
            throw new EntityNotFoundException("Запрос с id = " + requestId + " не существует");
        });
    }
}
