package wanderhub.server.domain.bo_comment_heart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.bo_comment_heart.entity.BoCommentHeart;

import java.util.Optional;

@Repository
public interface BoCommentHeartRepository extends JpaRepository<BoCommentHeart, Long> {

    @Query("select bch from BoCommentHeart as bch " +
            "where bch.boComment.boCommentId = :boCommentId and bch.member.Id = :memberId")
    Optional<BoCommentHeart> findByBoCommentAndMember(Long boCommentId, Long memberId);
}
