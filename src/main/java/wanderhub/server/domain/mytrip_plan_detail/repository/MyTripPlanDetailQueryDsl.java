package wanderhub.server.domain.mytrip_plan_detail.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.mytrip_plan_detail.dto.MyTripPlanDetailResponseDto;
import wanderhub.server.domain.mytrip_plan_detail.dto.QMyTripPlanDetailResponseDto;

import javax.persistence.EntityManager;
import static wanderhub.server.domain.mytrip_plan_detail.dto.QMyTripPlanDetailResponseDto.*;
import static wanderhub.server.domain.mytrip_plan_detail.entity.QMyTripPlanDetail.*;

@Repository
public class MyTripPlanDetailQueryDsl {
    private final JPAQueryFactory queryFactory;

    public MyTripPlanDetailQueryDsl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public MyTripPlanDetailResponseDto getMyTripPlanDetail(Long myTripPlanDetailId) {
        MyTripPlanDetailResponseDto myTripPlanDetailResponseDto;
        myTripPlanDetailResponseDto = queryFactory
                .select(new QMyTripPlanDetailResponseDto(
                        myTripPlanDetail.myTripPlanDetailId,
                        myTripPlanDetail.subTitle,
                        myTripPlanDetail.content,
                        myTripPlanDetail.coordinateX,
                        myTripPlanDetail.coordinateY,
                        myTripPlanDetail.placeName,
                        myTripPlanDetail.whenDate,
                        myTripPlanDetail.timeStart,
                        myTripPlanDetail.timeEnd,
                        myTripPlanDetail.createdAt,
                        myTripPlanDetail.modifiedAt
                ))
                .from(myTripPlanDetail)
                .where(myTripPlanDetail.myTripPlanDetailId.eq(myTripPlanDetailId))
                .fetchOne();
        return myTripPlanDetailResponseDto;
    }
}
