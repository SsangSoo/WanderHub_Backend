package wanderhub.server.domain.accompany.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * wanderhub.server.domain.accompany.dto.QAccompanySingleResponseDTO is a Querydsl Projection type for AccompanySingleResponseDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAccompanySingleResponseDTO extends ConstructorExpression<AccompanySingleResponseDTO> {

    private static final long serialVersionUID = -720682462L;

    public QAccompanySingleResponseDTO(com.querydsl.core.types.Expression<Long> accompanyId, com.querydsl.core.types.Expression<String> accompanyMaker, com.querydsl.core.types.Expression<wanderhub.server.global.utils.Local> local, com.querydsl.core.types.Expression<Long> maxMemberCount, com.querydsl.core.types.Expression<java.time.LocalDate> accompanyStartDate, com.querydsl.core.types.Expression<java.time.LocalDate> accompanyEndDate, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Boolean> recruitComplete, com.querydsl.core.types.Expression<Double> coordinateX, com.querydsl.core.types.Expression<Double> coordinateY, com.querydsl.core.types.Expression<String> placeName, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifiedAt) {
        super(AccompanySingleResponseDTO.class, new Class<?>[]{long.class, String.class, wanderhub.server.global.utils.Local.class, long.class, java.time.LocalDate.class, java.time.LocalDate.class, String.class, String.class, boolean.class, double.class, double.class, String.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, accompanyId, accompanyMaker, local, maxMemberCount, accompanyStartDate, accompanyEndDate, title, content, recruitComplete, coordinateX, coordinateY, placeName, createdAt, modifiedAt);
    }

}

