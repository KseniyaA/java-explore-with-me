package ru.practicum.ewm.request;

import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.model.ParticipantRequest;

import java.util.List;
import java.util.Map;

public interface RequestService {
    ParticipantRequest add(long userId, long eventId);

    List<ParticipantRequest> getRequests(long userId);

    List<ParticipantRequest> getRequests(long userId, long eventId);

    ParticipantRequest cancelRequest(long userId, long requestId);

    EventRequestStatusUpdateResult changeRequestsStatus(long userId, long eventId,
                                                        List<Long> requestsIds, RequestStatus status);

    List<ParticipantRequest> getConfirmedRequests(Event event);

    Map<Long, Integer> getAllConfirmedRequests();
}
