package wanderhub.server.domain.bo_comment_heart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.bo_comment_heart.entity.BoCommentHeart;

@Repository
public interface BoCommentHeartRepository extends JpaRepository<BoCommentHeart, Long> {

}
