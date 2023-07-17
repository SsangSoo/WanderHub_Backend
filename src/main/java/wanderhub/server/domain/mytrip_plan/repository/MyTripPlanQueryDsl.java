package wanderhub.server.domain.mytrip_plan.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.accompany.dto.AccompanyResponseDto;
import wanderhub.server.domain.accompany.dto.QAccompanyResponseDto;
import wanderhub.server.domain.mytrip_plan.dto.*;

import javax.persistence.EntityManager;

import java.util.List;

import static wanderhub.server.domain.mytrip_plan.entity.QMyTripPlan.*;
import static wanderhub.server.domain.mytrip_plan.dto.QMyTripPlanListResponseDto.*;

@Repository
public class MyTripPlanQueryDsl {

    private final JPAQueryFactory queryFactory;

    public MyTripPlanQueryDsl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public MyTripPlanResponseDto getMyTripPlan(Long myTripPlanId) {
        MyTripPlanResponseDto myTripPlanResponseDto;
        myTripPlanResponseDto = queryFactory
                .select(new QMyTripPlanResponseDto(
                        myTripPlan.myTripPlanId.as("myTripPlanId"),
                        myTripPlan.title,
                        myTripPlan.tripStartDate,
                        myTripPlan.tripEndDate,
                        myTripPlan.createdAt,
                        myTripPlan.modifiedAt
                ))
                .from(myTripPlan)
                .where(myTripPlan.myTripPlanId.eq(myTripPlanId))
                .fetchOne();

//        List<>

        return myTripPlanResponseDto;
    }

    public List<MyTripPlanListResponseDto> getMyTripPlanList(Long memberId) {
        List<MyTripPlanListResponseDto> myTripPlanListResponseDtoList;
        myTripPlanListResponseDtoList = queryFactory
                .select(new QMyTripPlanListResponseDto(
                        myTripPlan.myTripPlanId.as("myTripPlanId"),
                        myTripPlan.title,
                        myTripPlan.tripStartDate,
                        myTripPlan.tripEndDate,
                        myTripPlan.createdAt
                ))
                .from(myTripPlan)
                .where(myTripPlan.member.Id.eq(memberId))
                .fetch();
        return myTripPlanListResponseDtoList;
    }
}
