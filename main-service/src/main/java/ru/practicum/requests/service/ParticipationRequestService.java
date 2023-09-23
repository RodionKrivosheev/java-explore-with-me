package ru.practicum.requests.service;

import ru.practicum.requests.model.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    List<ParticipationRequestDto> getAllRequestsByUserId(Long id);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
