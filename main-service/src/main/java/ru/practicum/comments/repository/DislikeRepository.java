package ru.practicum.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comments.model.entity.CommentDislike;

import java.util.List;
import java.util.Optional;

public interface DislikeRepository extends JpaRepository<CommentDislike, Long> {

    Boolean existsByCommentIdAndUserId(Long commentId, Long userId);

    Optional<CommentDislike> getByCommentIdAndUserId(Long commentId, Long userId);

    Integer countAllByCommentId(Long commentId);

    void deleteAllByCommentId(Long commentId);

    List<CommentDislike> findAllByCommentIdIn(List<Long> comments);
}
