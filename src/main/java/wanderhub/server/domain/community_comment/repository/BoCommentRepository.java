package wanderhub.server.domain.community_comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.community_comment.entity.BoComment;

@Repository
public interface BoCommentRepository extends JpaRepository<BoComment, Long> {

}
