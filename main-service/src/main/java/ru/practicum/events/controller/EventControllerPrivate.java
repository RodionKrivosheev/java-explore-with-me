package ru.practicum.events.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.model.dto.*;
import ru.practicum.events.service.EventService;
import ru.practicum.requests.model.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
public class EventControllerPrivate {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllEventsByUserId(@PathVariable Long userId,
                                                    @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                    @Valid @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET '/users/{}/events' получаем все ивенты по юзеру с id={}", userId, userId);
        return eventService.getAllEventsByUserId(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @Valid @RequestBody NewEventDto newEventDto) {
        log.info("POST '/users/{}/events' создаем новый ивент с body={} " +
                "from user with id={}", userId, newEventDto.toString(), userId);
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        log.info("GET '/users/{}/events/{}' получаем ивент по id={}", userId, eventId, eventId);
        return eventService.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("PATCH at '/users/{}/events/{}' to update event with body={}",
                userId, eventId, updateEventUserRequest.toString());
        return eventService.updateEventByUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequests(@Valid @Positive @PathVariable Long userId,
                                                     @Valid @Positive @PathVariable Long eventId) {
        log.info("GET at '/users/{}/events/{}/requests' to get requests by event with id={}", userId, eventId, eventId);
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestStatus(@Valid @Positive @PathVariable Long userId,
                                                              @Valid @Positive @PathVariable Long eventId,
                                                              @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("PATCH at '/users/{}/events/{}/requests' to update request with body={}",
                userId, eventId, eventRequestStatusUpdateRequest.toString());
        return eventService.updateRequestStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
