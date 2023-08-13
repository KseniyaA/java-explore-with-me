package ru.practicum.ewm.request;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.ewm.request.dto.ParticipantRequestDto;
import ru.practicum.ewm.request.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.model.ParticipantRequest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ParticipantRequestMapper {
    public ParticipantRequestDto toParticipantRequestDto(ParticipantRequest request) {
        return ParticipantRequestDto.builder()
                .requester(request.getRequester().getId())
                .id(request.getId())
                .created(request.getCreated())
                .status(request.getStatus().name())
                .event(request.getEvent().getId())
                .build();
    }

    public EventRequestStatusUpdateResultDto toEventRequestStatusUpdateRequestDto(
            EventRequestStatusUpdateResult requestStatusUpdateResult) {
        List<ParticipantRequestDto> confirmed = requestStatusUpdateResult.getConfirmedRequests() == null
                ? Collections.emptyList()
                : requestStatusUpdateResult.getConfirmedRequests().stream()
                    .map(ParticipantRequestMapper::toParticipantRequestDto)
                    .collect(Collectors.toList());
        List<ParticipantRequestDto> rejected = requestStatusUpdateResult.getRejectedRequests() == null ? Collections.emptyList() :
                requestStatusUpdateResult.getRejectedRequests().stream()
                .map(ParticipantRequestMapper::toParticipantRequestDto)
                .collect(Collectors.toList());
        return EventRequestStatusUpdateResultDto.builder()
                .confirmedRequests(confirmed)
                .rejectedRequests(rejected)
                .build();
    }
}
