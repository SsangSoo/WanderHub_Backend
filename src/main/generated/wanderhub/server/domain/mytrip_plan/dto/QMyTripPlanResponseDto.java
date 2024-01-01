package wanderhub.server.domain.mytrip_plan.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * wanderhub.server.domain.mytrip_plan.dto.QMyTripPlanResponseDto is a Querydsl Projection type for MyTripPlanResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMyTripPlanResponseDto extends ConstructorExpression<MyTripPlanResponseDto> {

    private static final long serialVersionUID = 431871705L;

    public QMyTripPlanResponseDto(com.querydsl.core.types.Expression<Long> myTripPlanId, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<java.time.LocalDate> tripStartDate, com.querydsl.core.types.Expression<java.time.LocalDate> tripEndDate, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifiedAt) {
        super(MyTripPlanResponseDto.class, new Class<?>[]{long.class, String.class, java.time.LocalDate.class, java.time.LocalDate.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, myTripPlanId, title, tripStartDate, tripEndDate, createdAt, modifiedAt);
    }

}

