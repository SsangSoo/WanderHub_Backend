package wanderhub.server.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.accompany.dto.AccompanyResponseListDto;
import wanderhub.server.domain.accompany.dto.QAccompanyResponseListDto;
import wanderhub.server.domain.board.dto.BoardListResponseDto;
import wanderhub.server.domain.board.dto.QBoardListResponseDto;
import wanderhub.server.domain.board_heart.entity.QBoardHeart;

import javax.persistence.EntityManager;
import java.util.List;

import static  wanderhub.server.domain.board.dto.QBoardListResponseDto.*;
import static wanderhub.server.domain.board.entity.QBoard.*;
import static wanderhub.server.domain.board_heart.entity.QBoardHeart.*;
import static wanderhub.server.domain.accompany.dto.QAccompanyResponseListDto.*;
import static wanderhub.server.domain.accompany.entity.QAccompany.*;


@Repository
public class MemberSearchQueryDsl {
    private final JPAQueryFactory queryFactory;

    public MemberSearchQueryDsl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    // 내가 만든 게시판
    public List<BoardListResponseDto> getWriteBoardList(String nickname) {
        List<BoardListResponseDto> boardListResponseDtoList;
        boardListResponseDtoList = queryFactory
                .select(new QBoardListResponseDto(
                        board.boardId,
                        board.nickName,
                        board.title,
                        board.local.stringValue(),
                        board.viewPoint,
                        board.boardHeartList.size().longValue(),
                        board.createdAt
                ))
                .from(board)
                .where(board.nickName.eq(nickname))
                .fetch();
        return boardListResponseDtoList;

    }

    // 내가 좋아요 한 게시판
    public List<BoardListResponseDto> getWriteBoardListWithHeart(Long memberId) {
        List<BoardListResponseDto> boardListResponseDtoList;
        boardListResponseDtoList = queryFactory
                .select(new QBoardListResponseDto(
                        board.boardId,
                        board.nickName,
                        board.title,
                        board.local.stringValue(),
                        board.viewPoint,
                        board.boardHeartList.size().longValue(),
                        board.createdAt
                ))
                .from(board)
                .where(board.boardHeartList.any().member.Id.eq(memberId)
                )
                .fetch();
        return boardListResponseDtoList;
    }

    // 내가 댓글 달은 게시판
    public List<BoardListResponseDto> getBoardWithWriteMyBoardComment(String nickName) {
        List<BoardListResponseDto> boardListResponseDtoList;
        boardListResponseDtoList = queryFactory
                .select(new QBoardListResponseDto(
                        board.boardId,
                        board.nickName,
                        board.title,
                        board.local.stringValue(),
                        board.viewPoint,
                        board.boardHeartList.size().longValue(),
                        board.createdAt
                ))
                .from(board)
                .where(board.boCommentList.any().nickName.eq(nickName))
                .fetch();
        return boardListResponseDtoList;
    }

    // 내가 좋아요 달은 댓글이 있는 게시판
    public List<BoardListResponseDto> getBoardWithWriteHeartBoardComment(Long memberId) {
        List<BoardListResponseDto> boardListResponseDtoList;
        boardListResponseDtoList = queryFactory
                .select(new QBoardListResponseDto(
                        board.boardId,
                        board.nickName,
                        board.title,
                        board.local.stringValue(),
                        board.viewPoint,
                        board.boardHeartList.size().longValue(),
                        board.createdAt
                ))
                .from(board)
                .where(board.boCommentList.any().boCommentHeartList.any().member.Id.eq(memberId))
                .fetch();
        return boardListResponseDtoList;
    }

    // 내가 만등 동행
    public List<AccompanyResponseListDto> getWriteAccompanList(String nickname) {
        List<AccompanyResponseListDto> accompanyListResponseDtoList;
        accompanyListResponseDtoList = queryFactory
                .select(new QAccompanyResponseListDto(
                        accompany.accompanyId,
                        accompany.nickname,
                        accompany.local.stringValue(),
                        accompany.accompanyMemberList.size().longValue(),
                        accompany.maxMemberNum,
                        accompany.accompanyStartDate,
                        accompany.accompanyEndDate,
                        accompany.title,
                        accompany.recruitComplete,
                        accompany.createdAt
                ))
                .from(accompany)
                .where(accompany.nickname.eq(nickname))
                .fetch();
        return accompanyListResponseDtoList;
    }

    // 내가 참여 중인 동행
    public List<AccompanyResponseListDto> getWriteAccompanyJoined(String nickName) {
        List<AccompanyResponseListDto> accompanyListResponseDtoList;
        accompanyListResponseDtoList = queryFactory
                .select(new QAccompanyResponseListDto(
                        accompany.accompanyId,
                        accompany.nickname,
                        accompany.local.stringValue(),
                        accompany.accompanyMemberList.size().longValue(),
                        accompany.maxMemberNum,
                        accompany.accompanyStartDate,
                        accompany.accompanyEndDate,
                        accompany.title,
                        accompany.recruitComplete,
                        accompany.createdAt
                ))
                .from(accompany)
                .where(accompany.accompanyMemberList.any().member.nickName.eq(nickName))
                .fetch();
        return accompanyListResponseDtoList;
    }

}