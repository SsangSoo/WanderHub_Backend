package wanderhub.server.domain.mytrip_plan.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * wanderhub.server.domain.mytrip_plan.dto.QMyTripPlanListResponseDto is a Querydsl Projection type for MyTripPlanListResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMyTripPlanListResponseDto extends ConstructorExpression<MyTripPlanListResponseDto> {

    private static final long serialVersionUID = 754182619L;

    public QMyTripPlanListResponseDto(com.querydsl.core.types.Expression<Long> myTripPlanId, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<java.time.LocalDate> tripStartDate, com.querydsl.core.types.Expression<java.time.LocalDate> tripEndDate, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt) {
        super(MyTripPlanListResponseDto.class, new Class<?>[]{long.class, String.class, java.time.LocalDate.class, java.time.LocalDate.class, java.time.LocalDateTime.class}, myTripPlanId, title, tripStartDate, tripEndDate, createdAt);
    }

}

