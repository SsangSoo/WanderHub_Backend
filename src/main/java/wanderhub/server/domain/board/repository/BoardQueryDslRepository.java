package wanderhub.server.domain.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.bo_comment.dto.BoCommentResponseDto;
import wanderhub.server.domain.bo_comment.dto.QBoCommentResponseDto;
import wanderhub.server.domain.board.dto.BoardListResponseDto;
import wanderhub.server.domain.board.dto.BoardResponseDto;
import wanderhub.server.domain.board.dto.QBoardListResponseDto;
import wanderhub.server.domain.board.dto.QBoardResponseDto;
import wanderhub.server.global.response.PageResponseDto;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static wanderhub.server.domain.board.entity.QBoard.*;
import static wanderhub.server.domain.bo_comment.entity.QBoComment.*;

@Repository
public class BoardQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    public BoardQueryDslRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public PageResponseDto<BoardListResponseDto> searchBoard(Integer page) {
        List<BoardListResponseDto> boardDtoList;
        boardDtoList = queryFactory
                .select(new QBoardListResponseDto(
                        board.boardId.as("boardId"),
                        board.nickName,
                        board.title,
                        board.local.stringValue(),
                        board.viewPoint,
                        board.boardHeartList.size().longValue(),
                        board.createdAt
                ))
                .from(board)
                .offset(page * 10 - 10)
                .limit(10)
                .fetch();
        Long totalElements = queryFactory
                .select(board.count())
                .from(board)
                .fetchOne();

        Long totalPage = (totalElements / 10) + (totalElements % 10 > 0 ? 1 : 0);  // totalPage

        // 마지막 페이지보다 작으면 10 아니라면, 총 요소갯수에서 % 10(페이지 사이즈)
        Long currentPageElements = page < totalPage ? 10 : totalElements % 10;

        return PageResponseDto.of(boardDtoList, totalPage, totalElements, currentPageElements, page);
    }

    // 게시판 단일 조회시 viewPoint 증가.
    public void updateViewPoint(Long boardId) {
        queryFactory
                .update(board)
                .set(board.viewPoint, board.viewPoint.add(1))
                .where(board.boardId.eq(boardId))
                .execute();
    }

    public BoardResponseDto getResultBoard(Long boardId) {
        BoardResponseDto boardResponseDto;
        boardResponseDto = queryFactory
                .select(new QBoardResponseDto(
                        board.boardId,
                        board.nickName,
                        board.title,
                        board.content,
                        board.local.stringValue(),
                        board.viewPoint,
                        board.boardHeartList.size().longValue(),
                        board.createdAt,
                        board.modifiedAt))
                .from(board)
                .where(board.boardId.eq(boardId))
                .fetchOne();

        boardResponseDto.setBoComments(getBoardCommentList(boardId));

        return boardResponseDto;
    }

    public List<BoCommentResponseDto> getBoardCommentList(Long boardId) {
        List<BoCommentResponseDto> boCommentResponseDtoList = queryFactory
                .select(new QBoCommentResponseDto(
                        boComment.boCommentId,
                        boComment.board.boardId,
                        boComment.nickName,
                        boComment.content,
                        boComment.boCommentHeartList.size().longValue(),
                        boComment.createdAt,
                        board.modifiedAt
                ))
                .from(boComment)
                .where(boComment.board.boardId.eq(boardId))
                .fetch();

        return boCommentResponseDtoList;
    }
}
