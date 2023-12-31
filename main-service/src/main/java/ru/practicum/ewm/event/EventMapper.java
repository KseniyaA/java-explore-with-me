package ru.practicum.ewm.event;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.location.LocationParam;
import ru.practicum.ewm.location.LocationParamMapper;
import ru.practicum.ewm.user.UserMapper;

@UtilityClass
public class EventMapper {
    public Event toEvent(EventDtoRequest dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .category(dto.getCategory() != null ? Category.builder().id(dto.getCategory()).build() : null)
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle())
                .location(dto.getLocation() != null ? Location.builder().lat(dto.getLocation().getLat())
                        .lon(dto.getLocation().getLon()).build() : null)
                .locationParam(dto.getLocationParamId() != null
                        ? LocationParam.builder().id(dto.getLocationParamId()).build() : null)
                .stateAction(dto.getStateAction() == null ? null : EventStateAction.valueOf(dto.getStateAction()))
                .build();
    }

    public Event toEvent(UpdateEventAdminRequest dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .category(dto.getCategory() != null ? Category.builder().id(dto.getCategory()).build() : null)
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle())
                .location(dto.getLocation() != null ? Location.builder().lat(dto.getLocation().getLat())
                        .lon(dto.getLocation().getLon()).build() : null)
                .locationParam(dto.getLocationParamId() != null
                        ? LocationParam.builder().id(dto.getLocationParamId()).build() : null)
                .stateAction(dto.getStateAction() == null ? null : EventStateAction.valueOf(dto.getStateAction()))
                .build();
    }

    public EventFullDtoResponse toEventFullDtoResponse(Event event, Integer confirmedRequests) {
        return EventFullDtoResponse.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDtoResponse(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(LocationDto.builder().lat(event.getLocation().getLat()).lon(event.getLocation().getLon()).build())
                .locationParam(event.getLocationParam() != null
                        ? LocationParamMapper.toLocationDtoResponse(event.getLocationParam())
                        : null)
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getEventStatus().name())
                .title(event.getTitle())
                .views(event.getViews())
                .initiator(UserMapper.toUserShortDtoResponse(event.getInitiator()))
                .build();
    }

    public EventShortDtoResponse toEventShortDtoResponse(Event event, Integer confirmedRequests) {
        return EventShortDtoResponse.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDtoResponse(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
}
