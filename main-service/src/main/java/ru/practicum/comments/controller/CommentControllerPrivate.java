package ru.practicum.comments.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.model.dto.CommentDto;
import ru.practicum.comments.model.dto.NewCommentDto;
import ru.practicum.comments.model.dto.UpdateCommentRequest;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
public class CommentControllerPrivate {

    private CommentService commentService;

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("POST at '/users/{}/events/{}/comments' чтобы создать comment к event c id={}", userId, eventId, eventId);
        return commentService.createComment(userId, eventId, newCommentDto);
    }

    @PostMapping("/users/{userId}/comments/{commentId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public void addLikeToComment(@PathVariable Long userId,
                                 @PathVariable Long commentId) {
        log.info("POST at '/users/{}/comments/{}/like' чтобы лайкнуть comment с id={}", userId, commentId, commentId);
        commentService.addLikeToComment(userId, commentId);
    }

    @PostMapping("/users/{userId}/comments/{commentId}/dislike")
    @ResponseStatus(HttpStatus.CREATED)
    public void addDislikeToComment(@PathVariable Long userId,
                                    @PathVariable Long commentId) {
        log.info("POST at '/users/{}/comments/{}/dislike' чтобы дизлайкнуть comment с id={}", userId, commentId, commentId);
        commentService.addDislikeToComment(userId, commentId);
    }

    @PatchMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateCommentById(@PathVariable Long userId,
                                        @PathVariable Long commentId,
                                        @Valid @RequestBody UpdateCommentRequest updateCommentRequest) {
        log.info("PATCH at '/users/{}/comments/{}' чтобы обновить comment с id={}", userId, commentId, commentId);
        return commentService.updateComment(userId, commentId, updateCommentRequest);
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByUser(@PathVariable Long userId,
                                    @PathVariable Long commentId) {
        log.info("DELETE at '/users/{}/comments/{}' чтобы удалить comment с id={}", userId, commentId, commentId);
        commentService.deleteCommentByUser(userId, commentId);
    }
}
