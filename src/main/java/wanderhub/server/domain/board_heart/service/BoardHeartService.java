package wanderhub.server.domain.board_heart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.board.entity.Board;
import wanderhub.server.domain.board_heart.entity.BoardHeart;
import wanderhub.server.domain.board_heart.repository.BoardHeartRepository;
import wanderhub.server.domain.member.entity.Member;

import java.util.Optional;

@Service
@Transactional
public class BoardHeartService {

    private final BoardHeartRepository boardHeartRepository;

    public BoardHeartService(BoardHeartRepository boardHeartRepository) {
        this.boardHeartRepository = boardHeartRepository;
    }

    public void createBoardHeart(Member findMember, Board findBoard) {
        BoardHeart createdBoard = BoardHeart.builder()
                .member(findMember)
                .board(findBoard)
                .build();

        boardHeartRepository.save(createdBoard);
    }


    public Optional<BoardHeart> findByMemberAndBoard(Long boardId, String email) {
        return boardHeartRepository.findByMemberAndBoard(boardId, email);
    }

    public void removeBoardHeart(BoardHeart boardHeartWillRemove) {
        boardHeartRepository.delete(boardHeartWillRemove);
    }

}
