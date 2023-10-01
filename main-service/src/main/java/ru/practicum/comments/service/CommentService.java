package ru.practicum.comments.service;

import ru.practicum.comments.model.dto.CommentDto;
import ru.practicum.comments.model.dto.NewCommentDto;
import ru.practicum.comments.model.dto.UpdateCommentRequest;

import java.util.List;

public interface CommentService {

    CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    void addLikeToComment(Long userId, Long commentId);

    void addDislikeToComment(Long userId, Long commentId);

    CommentDto updateComment(Long userId, Long commentId, UpdateCommentRequest updateCommentRequest);

    CommentDto getCommentById(Long commentId);

    List<CommentDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size);

    Long getCommentsCountByEventId(Long eventId);

    void deleteCommentByUser(Long userId, Long commentId);

    void deleteCommentByAdmin(Long commentId);

    void deleteLike(Long userId, Long commentId);

    void deleteDislike(Long userId, Long commentId);
}
