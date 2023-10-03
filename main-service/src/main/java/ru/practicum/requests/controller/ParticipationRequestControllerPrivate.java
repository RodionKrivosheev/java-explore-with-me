package ru.practicum.requests.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.model.ParticipationRequestDto;
import ru.practicum.requests.service.ParticipationRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@AllArgsConstructor
public class ParticipationRequestControllerPrivate {

    private final ParticipationRequestService requestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getAll(@PathVariable Long userId) {
        log.info("GET '/users/{userId}/requests' получаем все requests по user с id={}", userId);
        return requestService.getAllRequestsByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable Long userId,
                                          @Valid @Positive @RequestParam Long eventId) {
        log.info("POST '/users/{}/requests' создаем request из user с id={} и event с id={}", userId, userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancel(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        log.info("PATCH '/users/{}/requests/{}/cancel' отмена request с id={} по user с id={}", userId, requestId, requestId, userId);
        return requestService.cancelRequest(userId, requestId);
    }
}
