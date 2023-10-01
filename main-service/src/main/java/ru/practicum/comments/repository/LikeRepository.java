package ru.practicum.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comments.model.entity.CommentLike;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<CommentLike, Long> {

    Boolean existsByCommentIdAndUserId(Long commentId, Long userId);

    Optional<CommentLike> getByCommentIdAndUserId(Long commentId, Long userId);

    Integer countAllByCommentId(Long commentId);

    void deleteAllByCommentId(Long commentId);

    List<CommentLike> findAllByCommentIdIn(List<Long> comments);
}
