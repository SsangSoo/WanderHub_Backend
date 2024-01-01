package wanderhub.server.domain.board.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * wanderhub.server.domain.board.dto.QBoardListResponseDto is a Querydsl Projection type for BoardListResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBoardListResponseDto extends ConstructorExpression<BoardListResponseDto> {

    private static final long serialVersionUID = 2054173302L;

    public QBoardListResponseDto(com.querydsl.core.types.Expression<Long> boardId, com.querydsl.core.types.Expression<String> nickName, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> local, com.querydsl.core.types.Expression<Long> viewPoint, com.querydsl.core.types.Expression<Long> likePoint, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt) {
        super(BoardListResponseDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, long.class, long.class, java.time.LocalDateTime.class}, boardId, nickName, title, local, viewPoint, likePoint, createdAt);
    }

}

