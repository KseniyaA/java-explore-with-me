package ru.practicum.ewm.request;

import ru.practicum.ewm.request.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.model.ParticipantRequest;

import java.util.List;

public interface RequestService {
    ParticipantRequest add(long userId, long eventId);

    List<ParticipantRequest> getRequests(long userId);

    List<ParticipantRequest> getRequests(long userId, long eventId);

    ParticipantRequest cancelRequest(long userId, long requestId);

    EventRequestStatusUpdateResult changeRequestsStatus(long userId, long eventId,
                                                        List<Long> requestsIds, String status);
}
