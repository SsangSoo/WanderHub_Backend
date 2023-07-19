package wanderhub.server.domain.bo_comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.bo_comment.dto.BoCommentResponseDto;
import wanderhub.server.domain.bo_comment.dto.QBoCommentResponseDto;

import static wanderhub.server.domain.bo_comment.dto.QBoCommentResponseDto.*;
import static wanderhub.server.domain.bo_comment.entity.QBoComment.*;
import static wanderhub.server.domain.board.entity.QBoard.*;

import javax.persistence.EntityManager;

@Repository
public class BoCommentQueryDsl {
    private final JPAQueryFactory queryFactory;

    public BoCommentQueryDsl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    public BoCommentResponseDto getBoCommentResult(Long boCommentId) {
        BoCommentResponseDto boCommentResponseDto;
        boCommentResponseDto = queryFactory
                .select(new QBoCommentResponseDto(
                        boComment.boCommentId.as("boCommentId"),
                        boComment.board.boardId,
                        boComment.nickName,
                        boComment.content,
                        boComment.boCommentHeartList.size().longValue(),
                        boComment.createdAt,
                        boComment.modifiedAt
                ))
                .from(boComment)
                .where(boComment.boCommentId.eq(boCommentId))
                .fetchOne();
        return boCommentResponseDto;
    }


}
