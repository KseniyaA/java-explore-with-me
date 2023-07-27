package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.request.model.ParticipantRequest;
import ru.practicum.ewm.user.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipantRequest, Long> {
    ParticipantRequest findAllByRequesterAndEvent(User requester, Event event);

    List<ParticipantRequest> findAllByRequester(User requester);

    List<ParticipantRequest> findAllByEvent(Event event);

}
