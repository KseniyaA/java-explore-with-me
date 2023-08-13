package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.request.model.ParticipantRequest;
import ru.practicum.ewm.user.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipantRequest, Long> {
    ParticipantRequest findAllByRequesterAndEvent(User requester, Event event);

    List<ParticipantRequest> findAllByRequester(User requester);

    List<ParticipantRequest> findAllByEvent(Event event);

    List<ParticipantRequest> findAllByEventAndStatus(Event event, RequestStatus status);

    @Query(value = "select e.id as eventId, count(r.id) as requestsSize " +
            "from request as r inner join event as e on r.event_id = e.id " +
            "where r.status = ?1 " +
            "group by e.id ", nativeQuery = true)
    List<RequestsByStatus> getEventRequestsByStatus(String status);
}
