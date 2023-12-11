package wanderhub.server.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.accompany.dto.AccompanyListResponseDto;
import wanderhub.server.domain.accompany.dto.QAccompanyListResponseDto;
import wanderhub.server.domain.board.dto.BoardListResponseDto;
import wanderhub.server.domain.board.dto.QBoardListResponseDto;
import wanderhub.server.domain.board_heart.entity.QBoardHeart;
import wanderhub.server.domain.member.entity.MemberStatus;
import wanderhub.server.domain.member.entity.QMember;

import javax.persistence.EntityManager;
import java.util.List;

import static  wanderhub.server.domain.board.dto.QBoardListResponseDto.*;
import static wanderhub.server.domain.board.entity.QBoard.*;
import static wanderhub.server.domain.board_heart.entity.QBoardHeart.*;
import static wanderhub.server.domain.accompany.dto.QAccompanyListResponseDto.*;
import static wanderhub.server.domain.accompany.entity.QAccompany.*;
import static wanderhub.server.domain.member.entity.QMember.member;


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
    public List<AccompanyListResponseDto> getWriteAccompanList(String nickname) {
        List<AccompanyListResponseDto> accompanyListResponseDtoList;
        accompanyListResponseDtoList = queryFactory
                .select(new QAccompanyListResponseDto(
                        accompany.accompanyId,
                        accompany.accompanyMaker,
                        accompany.local.stringValue(),
                        accompany.accompanyMemberList.size(),
                        accompany.maxMemberCount,
                        accompany.accompanyStartDate,
                        accompany.accompanyEndDate,
                        accompany.title,
                        accompany.recruitComplete,
                        accompany.createdAt
                ))
                .from(accompany)
                .where(accompany.accompanyMaker.eq(nickname))
                .fetch();
        return accompanyListResponseDtoList;
    }

    // 내가 참여 중인 동행
    public List<AccompanyListResponseDto> getWriteAccompanyJoined(String nickName) {
        List<AccompanyListResponseDto> accompanyListResponseDtoList;
        accompanyListResponseDtoList = queryFactory
                .select(new QAccompanyListResponseDto(
                        accompany.accompanyId,
                        accompany.accompanyMaker,
                        accompany.local.stringValue(),
                        accompany.accompanyMemberList.size(),
                        accompany.maxMemberCount,
                        accompany.accompanyStartDate,
                        accompany.accompanyEndDate,
                        accompany.title,
                        accompany.recruitComplete,
                        accompany.createdAt
                ))
                .from(accompany)
                .where(accompany.accompanyMemberList.any().member.nickname.eq(nickName))
                .fetch();
        return accompanyListResponseDtoList;
    }

    public void quitMember(String nickname, Long memberId) {
        queryFactory.update(member)
                .set(member.memberStatus, MemberStatus.HUMAN)
                .set(member.nickname, nickname)
                .where(member.Id.eq(memberId))
                .execute();
    }

}