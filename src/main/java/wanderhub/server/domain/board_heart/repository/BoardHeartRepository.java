package wanderhub.server.domain.board_heart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.board_heart.entity.BoardHeart;

import java.util.Optional;

@Repository
public interface BoardHeartRepository extends JpaRepository<BoardHeart, Long> {

    @Query("select bh from BoardHeart as bh " +
            "where bh.board.boardId = :boardId and bh.member.email = :email")
    Optional<BoardHeart> findByMemberAndBoard(Long boardId, String email);



}
