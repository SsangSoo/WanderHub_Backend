package wanderhub.server.domain.mytrip_plan_detail.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * wanderhub.server.domain.mytrip_plan_detail.dto.QMyTripPlanDetailListResponseDto is a Querydsl Projection type for MyTripPlanDetailListResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMyTripPlanDetailListResponseDto extends ConstructorExpression<MyTripPlanDetailListResponseDto> {

    private static final long serialVersionUID = -1379195818L;

    public QMyTripPlanDetailListResponseDto(com.querydsl.core.types.Expression<Long> myTripPlanDetailId, com.querydsl.core.types.Expression<String> subTitle, com.querydsl.core.types.Expression<String> placeName, com.querydsl.core.types.Expression<java.time.LocalDate> whenDate, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt) {
        super(MyTripPlanDetailListResponseDto.class, new Class<?>[]{long.class, String.class, String.class, java.time.LocalDate.class, java.time.LocalDateTime.class}, myTripPlanDetailId, subTitle, placeName, whenDate, createdAt);
    }

}

