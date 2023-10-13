package ru.practicum.events.service;

import ru.practicum.enums.EventSortOptions;
import ru.practicum.events.model.dto.*;
import ru.practicum.events.model.entity.Event;
import ru.practicum.requests.model.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto updateEventByUser(
            Long userId,
            Long eventId,
            UpdateEventUserRequest updateEventUserRequest);

    EventRequestStatusUpdateResult updateRequestStatus(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    Event getEventModelById(Long id);

    List<EventShortDto> getAllEventsByUserId(Long userId, Integer from, Integer size);

    EventFullDto getEventById(Long userId, Long eventId);

    List<EventFullDto> getEventsByUserIds(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size);

    List<EventShortDto> getEventsByParams(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventSortOptions sort,
            Integer from,
            Integer size,
            HttpServletRequest request);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    EventFullDto getEventById(Long eventId, HttpServletRequest request);

    void eventExists(Long eventId);
}
