package wanderhub.server.domain.mytrip_plan_detail.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * wanderhub.server.domain.mytrip_plan_detail.dto.QMyTripPlanDetailResponseDto is a Querydsl Projection type for MyTripPlanDetailResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMyTripPlanDetailResponseDto extends ConstructorExpression<MyTripPlanDetailResponseDto> {

    private static final long serialVersionUID = -1389132076L;

    public QMyTripPlanDetailResponseDto(com.querydsl.core.types.Expression<Long> myTripPlanDetailId, com.querydsl.core.types.Expression<String> subTitle, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Double> coordinateX, com.querydsl.core.types.Expression<Double> coordinateY, com.querydsl.core.types.Expression<String> placeName, com.querydsl.core.types.Expression<java.time.LocalDate> whenDate, com.querydsl.core.types.Expression<java.time.LocalTime> timeStart, com.querydsl.core.types.Expression<java.time.LocalTime> timeEnd, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifiedAt) {
        super(MyTripPlanDetailResponseDto.class, new Class<?>[]{long.class, String.class, String.class, double.class, double.class, String.class, java.time.LocalDate.class, java.time.LocalTime.class, java.time.LocalTime.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, myTripPlanDetailId, subTitle, content, coordinateX, coordinateY, placeName, whenDate, timeStart, timeEnd, createdAt, modifiedAt);
    }

}

