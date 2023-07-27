package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestStatusUpdateResultDto {
    private List<ParticipantRequestDto> confirmedRequests;
    private List<ParticipantRequestDto> rejectedRequests;
}
