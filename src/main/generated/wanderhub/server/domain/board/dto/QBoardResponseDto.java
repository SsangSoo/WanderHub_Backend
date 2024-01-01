package wanderhub.server.domain.board.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * wanderhub.server.domain.board.dto.QBoardResponseDto is a Querydsl Projection type for BoardResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBoardResponseDto extends ConstructorExpression<BoardResponseDto> {

    private static final long serialVersionUID = 164930804L;

    public QBoardResponseDto(com.querydsl.core.types.Expression<Long> boardId, com.querydsl.core.types.Expression<String> nickName, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<String> local, com.querydsl.core.types.Expression<Long> viewPoint, com.querydsl.core.types.Expression<Long> likePoint, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifiedAt) {
        super(BoardResponseDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class, long.class, long.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, boardId, nickName, title, content, local, viewPoint, likePoint, createdAt, modifiedAt);
    }

}

