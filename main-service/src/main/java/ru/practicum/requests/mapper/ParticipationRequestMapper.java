package ru.practicum.requests.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.ParticipationRequestDto;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ParticipationRequestMapper {

    public ParticipationRequestDto toRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .created(participationRequest.getCreated())
                .event(participationRequest.getEvent().getId())
                .id(participationRequest.getId())
                .requester(participationRequest.getId())
                .status(participationRequest.getStatus())
                .build();
    }

    public List<ParticipationRequestDto> toRequestDto(Iterable<ParticipationRequest> eventRequests) {
        List<ParticipationRequestDto> result = new ArrayList<>();

        for (ParticipationRequest request : eventRequests) {
            result.add(toRequestDto(request));
        }

        return result;
    }
}
