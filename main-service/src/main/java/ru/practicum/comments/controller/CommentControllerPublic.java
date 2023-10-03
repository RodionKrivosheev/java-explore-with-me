package ru.practicum.comments.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.model.dto.CommentDto;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
public class CommentControllerPublic {

    private final CommentService commentService;

    @GetMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllCommentsByEventId(@PathVariable Long eventId,
                                                    @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                    @Valid @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET at '/events/{}/comments' чтобы получить все comments by event with id={}", eventId, eventId);
        return commentService.getAllCommentsByEventId(eventId, from, size);
    }

    @GetMapping("/events/{eventId}/comments/count")
    @ResponseStatus(HttpStatus.OK)
    public Long getCommentsCountByEventId(@PathVariable Long eventId) {
        log.info("GET at '/events/{eventId}/comments/count' чтобы получить comments count by event with id={}", eventId);
        return commentService.getCommentsCountByEventId(eventId);
    }
}
