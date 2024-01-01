package wanderhub.server.domain.bo_comment.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * wanderhub.server.domain.bo_comment.dto.QBoCommentResponseDto is a Querydsl Projection type for BoCommentResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBoCommentResponseDto extends ConstructorExpression<BoCommentResponseDto> {

    private static final long serialVersionUID = -1406358921L;

    public QBoCommentResponseDto(com.querydsl.core.types.Expression<Long> boCommentId, com.querydsl.core.types.Expression<Long> boardId, com.querydsl.core.types.Expression<String> nickName, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Long> likePoint, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifiedAt) {
        super(BoCommentResponseDto.class, new Class<?>[]{long.class, long.class, String.class, String.class, long.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, boCommentId, boardId, nickName, content, likePoint, createdAt, modifiedAt);
    }

}

