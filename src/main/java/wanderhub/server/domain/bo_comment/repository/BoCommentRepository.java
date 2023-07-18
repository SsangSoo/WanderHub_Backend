package wanderhub.server.domain.bo_comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.bo_comment.entity.BoComment;

import java.util.Optional;

@Repository
public interface BoCommentRepository extends JpaRepository<BoComment, Long> {

    @Query("select bc from BoComment as bc where bc.board.boardId = :boardId and bc.boCommentId = :boCommentId")
    Optional<BoComment> findByBoCommentId(Long boardId, Long boCommentId);
}
