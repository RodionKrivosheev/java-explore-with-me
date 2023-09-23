package ru.practicum.events.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.model.dto.EventFullDto;
import ru.practicum.events.service.EventService;
import ru.practicum.events.model.dto.UpdateEventAdminRequest;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@AllArgsConstructor
public class EventControllerAdmin {

    private final EventService eventService;
    @GetMapping("/admin/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsByUserIds(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET '/admin/events' получаем все ивенты с параметрами : users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsByUserIds(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("UPDATE '/admin/events/{}' обновляем ивенты с id={} и body={}", eventId, eventId, updateEventAdminRequest.toString());
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequest);
    }
}
