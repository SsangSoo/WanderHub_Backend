package wanderhub.server.domain.accompany.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * wanderhub.server.domain.accompany.dto.QAccompanyListResponseDto is a Querydsl Projection type for AccompanyListResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAccompanyListResponseDto extends ConstructorExpression<AccompanyListResponseDto> {

    private static final long serialVersionUID = -1157782772L;

    public QAccompanyListResponseDto(com.querydsl.core.types.Expression<Long> accompanyId, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<String> local, com.querydsl.core.types.Expression<Integer> currentMemberNum, com.querydsl.core.types.Expression<Long> maxMemberNum, com.querydsl.core.types.Expression<java.time.LocalDate> accompanyStartDate, com.querydsl.core.types.Expression<java.time.LocalDate> accompanyEndDate, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<Boolean> recruitComplete, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt) {
        super(AccompanyListResponseDto.class, new Class<?>[]{long.class, String.class, String.class, int.class, long.class, java.time.LocalDate.class, java.time.LocalDate.class, String.class, boolean.class, java.time.LocalDateTime.class}, accompanyId, nickname, local, currentMemberNum, maxMemberNum, accompanyStartDate, accompanyEndDate, title, recruitComplete, createdAt);
    }

}

