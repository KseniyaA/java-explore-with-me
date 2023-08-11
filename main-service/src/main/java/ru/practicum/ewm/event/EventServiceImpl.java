package ru.practicum.ewm.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.BadParameterException;
import ru.practicum.ewm.exception.ConflictOperationException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.UnavailableOperationException;
import ru.practicum.ewm.stats.StatsClientProvider;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.stats.dto.EndpointHitDtoRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    private StatsClientProvider statsClientProvider;

    private static Map<String, List<String>> uriIpMap = new HashMap<>();

    @Transactional
    @Override
    public Event add(Long userId, Event event) {
        User user = getUserById(userId);
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setEventStatus(EventStatus.CREATED);
        event.setViews(0);
        event.setConfirmedRequests(0);
        if (event.getPaid() == null) {
            event.setPaid(Boolean.FALSE);
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }

        Event createdEvent = eventRepository.save(event);
        updateStatusAfterCreate(createdEvent);
        return createdEvent;
    }

    @Transactional
    @Override
    public Event update(long userId, long eventId, Event newEvent) {
        getUserById(userId);
        Event oldEvent = getEventById(eventId);
        if (!oldEvent.getInitiator().getId().equals(userId)) {
            throw new UnavailableOperationException("Пользователь с id = " + userId + " не может редактировать событие, " +
                    "созданное пользователем с id = " + newEvent.getInitiator().getId());
        }
        if (newEvent.getStateAction() != null) {
            if (newEvent.getStateAction().equals(EventStateAction.SEND_TO_REVIEW)) {
                oldEvent.setEventStatus(EventStatus.PENDING);
            } else if (newEvent.getStateAction().equals(EventStateAction.CANCEL_REVIEW)) {
                oldEvent.setEventStatus(EventStatus.CANCELED);
            }
        }
        if (!(oldEvent.getEventStatus() == EventStatus.CANCELED
                || oldEvent.getEventStatus() == EventStatus.PENDING)) {
            throw new ConflictOperationException("Событие со статусом " + oldEvent.getEventStatus().name() +
                    " не доступно для редактирования.");
        }
        merge(oldEvent, newEvent);
        return oldEvent;
    }

    @Transactional
    @Override
    public Event updateByAdmin(long eventId, Event newEvent) {
        Event oldEvent = getEventById(eventId);
        merge(oldEvent, newEvent);
        if (EventStateAction.PUBLISH_EVENT.equals(newEvent.getStateAction())) {
            if (oldEvent.getEventStatus().equals(EventStatus.PUBLISHED)) {
                throw new ConflictOperationException("Событие уже опубликовано");
            }
            if (oldEvent.getEventStatus().equals(EventStatus.CANCELED)) {
                throw new ConflictOperationException("Невозможно опубликовать отмененное событие");
            }
            oldEvent.setEventStatus(EventStatus.PUBLISHED);
        } else if (EventStateAction.REJECT_EVENT.equals(newEvent.getStateAction())) {
            if (oldEvent.getEventStatus().equals(EventStatus.PUBLISHED)) {
                throw new ConflictOperationException("Нельзя отменить уже опубликованное событие");
            }
            oldEvent.setEventStatus(EventStatus.CANCELED);
        }
        return eventRepository.save(oldEvent);
    }

    @Override
    public Event get(long userId, long eventId) {
        return getEventById(eventId);
    }

    @Override
    public List<Event> getAll(long userId, Integer from, Integer size) {
        User initiator = getUserById(userId);
        Pageable page = PageRequest.of(from / size, size, Sort.by("id").ascending());
        return eventRepository.findByInitiator(initiator, page).getContent();
    }

    @Override
    public List<Event> getAll(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
                              Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request) {
        BooleanExpression expr = QEvent.event.eventStatus.eq(EventStatus.PUBLISHED);
        if (text != null) {
            expr = QEvent.event.annotation.toLowerCase().like(text.toLowerCase());
            expr.or(QEvent.event.description.toLowerCase().like(text.toLowerCase()));
        }
        if (categories != null && !categories.isEmpty()) {
            BooleanExpression categoriesExpr = QEvent.event.category.id.in(categories);
            expr = expr.and(categoriesExpr);
        }
        if (paid != null) {
            BooleanExpression paidExpr = QEvent.event.paid.eq(paid);
            expr = expr.and(paidExpr);
        }
        if (rangeStart != null && rangeEnd != null) {
            LocalDateTime startDate = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
            LocalDateTime endDate = LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER);
            if (startDate.isAfter(endDate)) {
                throw new BadParameterException("Дата начала превышает дату окончания");
            }
            BooleanExpression startExpr = QEvent.event.eventDate.after(startDate);
            BooleanExpression endExpr = QEvent.event.eventDate.before(endDate);
            expr = expr.and(startExpr).and(endExpr);
        } else {
            BooleanExpression startExpr = QEvent.event.eventDate.after(LocalDateTime.now());
            expr = expr.and(startExpr);
        }
        if (onlyAvailable) {
            BooleanExpression reqExpr = QEvent.event.confirmedRequests.lt(QEvent.event.participantLimit);
            expr = expr.and(reqExpr);
        }
        Sort sortBy;
        Pageable page;
        if ("EVENT_DATE".equals(sort)) {
            sortBy = Sort.by("eventDate").descending();
            page = PageRequest.of(from / size, size, sortBy);
        } else if ("VIEWS".equals(sort)) {
            sortBy = Sort.by("views").ascending();
            page = PageRequest.of(from / size, size, sortBy);
        } else {
            page = PageRequest.of(from / size, size);
        }

        Page<Event> events = eventRepository.findAll(expr, page);
        List<Event> eventList = events.getContent();
        sendStat(request);
        return eventList;
    }

    @Override
    public List<Event> getAllByParams(List<Long> users, List<String> states, List<Long> categories, String rangeStart,
                                      String rangeEnd, Integer from, Integer size) {

        BooleanExpression expr = null;
        if (users != null && !users.isEmpty()) {
            expr = QEvent.event.initiator.id.in(users);
        }
        if (states != null && !states.isEmpty()) {
            List<EventStatus> eventStates = states.stream().map(EventStatus::valueOf).collect(Collectors.toList());
            BooleanExpression eventStatusExpr = QEvent.event.eventStatus.in(eventStates);
            expr = expr != null ? expr.and(eventStatusExpr) : eventStatusExpr;
        }
        if (categories != null && !categories.isEmpty()) {
            BooleanExpression categoriesExpr = QEvent.event.category.id.in(categories);
            expr = expr != null ? expr.and(categoriesExpr) : categoriesExpr;
        }
        if (rangeStart != null) {
            BooleanExpression startExpr = QEvent.event.createdOn.after(LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER));
            expr = expr != null ? expr.and(startExpr) : startExpr;
        }
        if (rangeEnd != null) {
            BooleanExpression endExpr = QEvent.event.createdOn.before(LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER));
            expr = expr != null ? expr.and(endExpr) : endExpr;
        }
        Pageable page = PageRequest.of(from / size, size, Sort.by("id").ascending());
        Page<Event> events;
        if (expr != null) {
            events = eventRepository.findAll(expr, page);
        } else {
            events = eventRepository.findAll(page);
        }
        return events.getContent();
    }

    @Transactional
    @Override
    public Event getPublishedEvent(long eventId, HttpServletRequest request) {
        Event eventById = getEventById(eventId);
        if (!eventById.getEventStatus().equals(EventStatus.PUBLISHED)) {
            throw new EntityNotFoundException("Опубликованное событие с id = " + eventId + " не найдено");
        }
        sendStat(request);
        checkAndIncView(eventById, request);
        return eventById;
    }

    @Transactional
    public void checkAndIncView(Event event, HttpServletRequest request) {
        if (uriIpMap.containsKey(request.getRequestURI())) {
            List<String> ips = uriIpMap.get(request.getRequestURI());
            List<String> ip = ips.stream().filter(x -> x.equals(request.getRemoteAddr())).collect(Collectors.toList());
            if (ip.isEmpty()) {
                ips.add(request.getRemoteAddr());
            } else {
                Integer views = event.getViews();
                if (views == 0) {
                    event.setViews(++views);
                }
            }
        } else {
            uriIpMap.put(request.getRequestURI(), Arrays.asList(request.getRemoteAddr()));
        }
    }

    @Transactional
    private void merge(Event oldEvent, Event newEvent) {
        oldEvent.setAnnotation(newEvent.getAnnotation() != null ? newEvent.getAnnotation() : oldEvent.getAnnotation());
        oldEvent.setCategory(newEvent.getCategory() != null ? newEvent.getCategory() : oldEvent.getCategory());
        oldEvent.setDescription(newEvent.getDescription() != null ? newEvent.getDescription() : oldEvent.getDescription());
        oldEvent.setEventDate(newEvent.getEventDate() != null ? newEvent.getEventDate() : oldEvent.getEventDate());
        oldEvent.setLocation(newEvent.getLocation() != null ? newEvent.getLocation() : oldEvent.getLocation());
        oldEvent.setPaid(newEvent.getPaid() != null ? newEvent.getPaid() : oldEvent.getPaid());
        oldEvent.setParticipantLimit(newEvent.getParticipantLimit() != null ? newEvent.getParticipantLimit() : oldEvent.getParticipantLimit());
        oldEvent.setRequestModeration(newEvent.getRequestModeration() != null ? newEvent.getRequestModeration() :
                oldEvent.getRequestModeration());
        oldEvent.setTitle(newEvent.getTitle() != null ? newEvent.getTitle() : oldEvent.getTitle());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    private void updateStatusAfterCreate(Event event) {
        event.setEventStatus(EventStatus.PENDING);
        eventRepository.save(event);
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

    private void sendStat(HttpServletRequest request) {
        EndpointHitDtoRequest ewm = EndpointHitDtoRequest.builder()
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .app("ewm").build();
        statsClientProvider.getClient().sendStat(ewm);
    }
}
